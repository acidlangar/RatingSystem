
/****** Script for SelectTopNRows command from SSMS  ******/
use id_bt
go

-- 1. LIMPIAR TABLAS
delete hist_rendimiento where cd_metodo = 2

-- 3. GENERAR RUN
DECLARE @RC int

-- 5. GENERAR HIST_RENDIMIENTO
DECLARE @pCd_Lig int
DECLARE @pCd_Met int

EXECUTE @RC = [ID_BT].[dbo].[cal_hist_rendimiento] 
   @pCd_Lig = 1
  ,@pCd_Met = 2


-- 6. COMPROBAR QUE ESTAN LLENAS

select count(*) cant_pron_met2 from pronostico
where 
	cd_Metodo = 2

-- 7. CONSULTAR EL YIELD FINAL
select top 1 * 
from hist_rendimiento
where
	cd_metodo = 2
order by
	cd_Pronostico desc	

