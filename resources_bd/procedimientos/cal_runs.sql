USE [ID_BT]
GO

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[cal_runs]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[cal_runs]
GO 

set ANSI_NULLS ON
set QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[cal_runs] 
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @LimInf NUMERIC(5,3) 
	DECLARE @LimSup NUMERIC(5,3) 
	DECLARE @vFechaInicial SMALLDATETIME
	DECLARE @vFechaSinHora SMALLDATETIME
	DECLARE @vIdFecha INT
	DECLARE @vIdRun INT
	DECLARE @vMaxRun INT
	DECLARE @vAcierto SMALLINT
	DECLARE @vAcumulado INT
	DECLARE @vIdFAnterior INT
	DECLARE @vAcumAnterior INT
	
	SET @LimInf = 50
	SET @LimSup = 60
	SET @vIdFecha = 1
	

   TRUNCATE TABLE RUN	
   
	select 
		@vFechaInicial = min(sq.fecha)
	from 
	(
	select temporada, cd_liga, min(fecha) fecha
	from partido
	where
		(temporada = '2014' and cd_liga in (1,3,6))
		or	(temporada = '2014/2015' and cd_liga in (2,4,5,7,8,9))
	group by temporada, cd_liga
	) SQ

	-- crear cursor de las fecha 
	DECLARE cFechas CURSOR FAST_FORWARD  FOR 
		select distinct
			CONVERT(SMALLDATETIME,CONVERT(VARCHAR(10), fecha, 101), 101) fechaSinHora
		from partido
		where
			fecha >= @vFechaInicial
		order by 1
		

	set @vIdFecha = 1

	OPEN cFechas; 
	FETCH cFechas INTO @vFechaSinHora
	
	WHILE @@FETCH_STATUS = 0 BEGIN
	
		INSERT INTO [ID_BT].[dbo].[RUN]
           ([idFecha]
           ,[idRun]
           ,[Fecha]
           ,[cd_liga]
           ,[NomEqL]
           ,[Acierto]
           ,[Acumulado])
		SELECT 
			@vIdFecha idFecha,
			rank() over (order by P.equipo_local) as rank,
			P.fecha,
			P.cd_liga,
			P.equipo_local,
			case
				when P.goles_local > P.goles_visitante then 1 
				else 0
			end,
			NULL 
		FROM (
			SELECT *
			FROM PARTIDO
			WHERE
				cuota1 > 0
			--	and cd_liga = 1
		) P
		WHERE
			(1/P.cuota1)*100 >= @LimInf
			and (1/P.cuota1)*100 < @LimSup
			and convert(VARCHAR(10), P.fecha,101) = convert(VARCHAR(10), @vFechaSinHora,101)
			and
			 (
				(temporada = '2014' and P.cd_liga in (1,3,6))
				or	(temporada = '2014/2015' and P.cd_liga in (2,4,5,7,8,9))
			)
		order by
			P.equipo_local
		
	

		set @vIdFecha = @vIdFecha + 1

		FETCH cFechas INTO @vFechaSinHora
	END
	
	CLOSE cFechas;  
	DEALLOCATE cFechas;  

  
    -- se procede hacer el calculo de los acumulados
	SET @vIdRun = 1 
	
	SELECT
		@vMaxRun = MAX(idRun)
	FROM RUN
	
	WHILE @vIdRun <= @vMaxRun
	BEGIN
		DECLARE cAcumulado CURSOR FAST_FORWARD  FOR 
			SELECT
				idFecha,
				Acierto
			FROM RUN
			WHERE
				idRun = @vIdRun
			ORDER BY
				idFecha
				
		OPEN cAcumulado; 
		FETCH cAcumulado INTO @vIdFecha, @vAcierto
	
		--DECLARE @vAcumulado INT
		--DECLARE @vIdFAnterior INT
		SET @vAcumulado = 0
		SET @vAcumAnterior = 0
	
		WHILE @@FETCH_STATUS = 0 
		BEGIN
			SET @vIdFAnterior = NULL
		
			SELECT @vIdFAnterior = MAX(idFecha)	
			FROM RUN
			WHERE 
				idRun = @vIdRun
				AND idFecha < @vIdFecha
		
			IF @vAcierto = 1
			BEGIN
				UPDATE RUN SET acumulado = 0
				where	
					idFecha = @vIdFecha
					AND idRun = @vIdRun
			END		
			ELSE
			BEGIN 
				IF @vIdFAnterior IS NULL
					SET @vAcumAnterior = 0
				ELSE
				BEGIN
					SELECT 
						@vAcumAnterior = Acumulado
					FROM RUN
					WHERE	
						idFecha = @vIdFAnterior
						AND idRun = @vIdRun
				END 
				
				UPDATE RUN SET acumulado = @vAcumAnterior + 1
				WHERE	
					idFecha = @vIdFecha
					AND idRun = @vIdRun
			END
	
			FETCH cAcumulado INTO @vIdFecha, @vAcierto
		END
		
		CLOSE cAcumulado;  
		DEALLOCATE cAcumulado;  

	
		SET @vIdRun = @vIdRun + 1 
	END
  
END

