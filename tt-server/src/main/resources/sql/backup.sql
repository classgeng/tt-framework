

-- 将目标字符串 下划线转换成驼峰处理
SET @targetString = 'ABC_DEF_gh_iasdja'
SELECT GROUP_CONCAT(CONCAT(UPPER(SUBSTR(SUBSTRING_INDEX(SUBSTRING_INDEX(@targetString,'_',help_topic_id+1),'_',-1) ,1,1)), SUBSTR(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(@targetString,'_', help_topic_id+1),'_',-1)) ,2)) SEPARATOR '') AS num
FROM mysql.help_topic
WHERE help_topic_id <= LENGTH(@targetString) - LENGTH(replace(@targetString,'_',''))
ORDER BY help_topic_id DESC

-- 获取全局变量和会话变量
SHOW GLOBAL VARIABLES
SHOW SESSION VARIABLES

-- 支持字段模糊查询，等值查询
SHOW VARIABLES WHERE `Variable_name` LIKE '%log%'  LIMIT 10, 30
SHOW VARIABLES WHERE `Value` LIKE '%off%'


-- 获取存储函数
SELECT * FROM information_schema.Routines WHERE ROUTINE_NAME='priceIncrementByCat';
SHOW PROCEDURE STATUS WHERE `DB` != 'sys'
USE `nrce-09`;
SHOW CREATE PROCEDURE `priceIncrementByCat`

USE `nrce-03`;
SHOW CREATE PROCEDURE `PR_学分`

-- 获取触发器
USE `nrce-06`;
SHOW TRIGGERS;

SELECT * FROM information_schema.triggers WHERE trigger_name= 'tr_emp';

-- 获取自定义函数
show function status

use `nrce-03`;
show create function `fn_学分`;

-- 获取定时任务
SHOW EVENTS;