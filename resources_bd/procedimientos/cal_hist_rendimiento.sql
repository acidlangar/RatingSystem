USE [ID_BT]
GO

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[cal_hist_rendimiento]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[cal_hist_rendimiento]
GO

set ANSI_NULLS ON
set QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:		Isaac Ortiz
-- Create date: 20/07/2015
-- Description:	Llena la tabla de hist_rendimiento en base a tabla pronostico
-- Version 2 - Corrección del calculo del Yield - Realizada el 19Agosto2015 1pm - iortiz
-- =============================================
CREATE PROCEDURE [dbo].[cal_hist_rendimiento] 
(
	@pCd_Lig INT,
	@pCd_Met INT 
)
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @vCodigo INT
	DECLARE @vCodPronAnterior INT
	DECLARE @vMonDin DECIMAL(10,3)
	DECLARE @vStakeDin DECIMAL(10,3)
	DECLARE @vAcierto INT
	DECLARE @vcd_liga INT
	DECLARE @vfecha_str VARCHAR(10)
	DECLARE @vequipo_local VARCHAR(50)
	DECLARE @vresultado VARCHAR(1)
	DECLARE @vstake INT
	DECLARE @media DECIMAL(10,3)-- sp_help hist_rendimiento

	DECLARE @C1 FLOAT
	DECLARE @C2 FLOAT
	DECLARE @CX FLOAT
	DECLARE @Cfinal FLOAT
	DECLARE @vresult_real VARCHAR(1)

	DELETE HIST_RENDIMIENTO	WHERE CD_LIGA = @pCd_Lig AND CD_METODO = @pCd_Met

	DECLARE cPron CURSOR READ_ONLY FAST_FORWARD FOR
		SELECT 
			codigo, 
			cd_liga,
			fecha_str,
			equipo_local,
			resultado,
			stake
		FROM PRONOSTICO  -- SP_HELP PRONOSTICO
		WHERE
			cd_liga = ISNULL(@pCd_Lig,cd_liga)
			AND cd_metodo = @pCd_Met
		ORDER BY
			Codigo

	OPEN cPron
	FETCH NEXT FROM cPron INTO @vCodigo, @vcd_liga, @vfecha_str, @vequipo_local, @vresultado, @vstake
	
	WHILE @@FETCH_STATUS = 0
	BEGIN
		
		SELECT
			@C1 = cuota1,
			@C2 = cuota2,
			@CX = coutaX,
			@vresult_real = resultado
		FROM PARTIDO  --SP_HELP PARTIDO
		WHERE
			cd_liga = @vcd_liga
			AND fecha_str = @vfecha_str
			AND equipo_local = @vequipo_local
			
		IF @vresult_real = @vresultado
		BEGIN
			SET @vAcierto = 1
			
			IF @vresultado = '1'
				SET @Cfinal = @C1

			IF @vresultado = '2'
				SET @Cfinal = @C2

			IF @vresultado = 'x'
				SET @Cfinal = @CX

		END
		ELSE
		BEGIN
			SET @vAcierto = 0
		END


		/*select @pCd_Met Cd_Met, @pCd_Lig Cd_Lig, @vCodigo Codigo

				SELECT *
				FROM HIST_RENDIMIENTO
				WHERE
					cd_metodo = @pCd_Met
					AND ISNULL(cd_liga,1) = ISNULL(@pCd_Lig,1)
					AND cd_Pronostico < @vCodigo*/
			

		IF(EXISTS(
				SELECT 1
				FROM HIST_RENDIMIENTO
				WHERE
					cd_metodo = @pCd_Met
					AND ISNULL(cd_liga,1) = ISNULL(@pCd_Lig,1)
					AND cd_Pronostico < @vCodigo
			)
		) 
		BEGIN
			SELECT @vCodPronAnterior = MAX(cd_Pronostico)
			FROM HIST_RENDIMIENTO
			WHERE
				cd_metodo = @pCd_Met
				AND cd_liga = @pCd_Lig
				AND cd_Pronostico < @vCodigo

			SELECT @vMonDin = [monto_dinero]
			FROM HIST_RENDIMIENTO
			WHERE
				cd_metodo = @pCd_Met
				AND cd_liga = @pCd_Lig
				AND cd_Pronostico = @vCodPronAnterior
			
			--select 'existe y es ' + convert(varchar(30), @vMonDin)
		END
		ELSE
		BEGIN
			SET @vMonDin = 1000.00

			--select 'NO existe y es ' + convert(varchar(30), @vMonDin)
		END

				
		SET @vStakeDin = (@vstake * @vMonDin) / 100
		IF @vAcierto = 1
		BEGIN
			SET @vMonDin = @vMonDin + (@Cfinal * @vStakeDin) - @vStakeDin
		END
		ELSE
		BEGIN
			SET @vMonDin = @vMonDin - @vStakeDin
		END

		SELECT @media = AVG(convert(decimal(10,3),pr.stake))
		from pronostico pr
		where
			pr.codigo <= @vCodigo
			AND pr.cd_metodo = @pCd_Met
			AND pr.cd_liga = ISNULL(@pCd_Lig,pr.cd_liga) 
		

		INSERT INTO [ID_BT].[dbo].[HIST_RENDIMIENTO]
           ([cd_Metodo]
           ,[cd_Pronostico]
           ,[cd_liga]
           ,[acierto]
           ,[rend_stake]
           ,[yield]
           ,[yield_ls]
           ,[monto_dinero])
			   select 
					@pCd_Met,
					@vCodigo,
					@pCd_Lig,
					(sum(case 
						when pr.resultado = p.resultado then 1
						else 0
					end)*100)/count(1) Acierto,			
					sum(case 
						when pr.resultado = p.resultado 
							then ((case 
										when p.resultado = '1' then p.cuota1
										when p.resultado = '2' then p.cuota2
										else p.coutaX
									end) * pr.stake) - pr.stake
						else 
							-1 * pr.stake
					end) rend_stake,
					(( sum(case 
						when pr.resultado = p.resultado then ((case 
										when p.resultado = '1' then p.cuota1
										when p.resultado = '2' then p.cuota2
										else p.coutaX
									end) * pr.stake) - pr.stake
						else -1 * pr.stake
					end) ) / sum(stake)) * 100 Yield,
					(( sum(case 
						when pr.resultado = p.resultado then ((case 
										when p.resultado = '1' then p.cuota1
										when p.resultado = '2' then p.cuota2
										else p.coutaX
									end) * @media) - @media
						else -1 * pr.stake
					end) ) / sum(stake)) * 100 [yield_ls],
					@vMonDin [monto_dinero]
				from pronostico pr
				join partido p on p.cd_liga = pr.cd_liga and convert(varchar(10),p.fecha,101) = pr.fecha_str and p.equipo_local = pr.equipo_local
				where
					pr.codigo <= @vCodigo
					AND pr.cd_metodo = @pCd_Met
					AND pr.cd_liga = ISNULL(@pCd_Lig,pr.cd_liga) 
	
		FETCH NEXT FROM cPron INTO @vCodigo, @vcd_liga, @vfecha_str, @vequipo_local, @vresultado, @vstake
	END
	
	CLOSE cPron
	DEALLOCATE cPron

END

