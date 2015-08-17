use id_bt
go

-- 1. LIMPIAR TABLAS
delete run

delete hist_rendimiento where cd_metodo = 1

delete pronostico where cd_metodo = 1

-- 2. DEMOSTRACION Q ESTAN LIMPIAS LAS TABLAS
select count(*) cant_run from run

select count(*) cant_pron_met1 from pronostico
where 
	cd_Metodo = 1

-- 3. GENERAR RUN
DECLARE @RC int
EXECUTE @RC = [ID_BT].[dbo].[cal_runs] 

-- 4. GENERAR PRONOSTICO
EXECUTE @RC = [ID_BT].[dbo].[cal_pronsticos_metodo_1] 

-- 5. GENERAR HIST_RENDIMIENTO
DECLARE @pCd_Lig int
DECLARE @pCd_Met int

EXECUTE @RC = [ID_BT].[dbo].[cal_hist_rendimiento] 
   @pCd_Lig = NULL
  ,@pCd_Met = 1


-- 6. COMPROBAR QUE ESTAN LLENAS
select count(*) cant_run from run

select count(*) cant_pron_met1 from pronostico
where 
	cd_Metodo = 1

-- 7. CONSULTAR EL YIELD FINAL
select top 1 * 
from hist_rendimiento
where
	cd_metodo = 1
order by
	cd_Pronostico desc	

