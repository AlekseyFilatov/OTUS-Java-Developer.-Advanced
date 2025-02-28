https://habr.com/ru/articles/548700/
https://habr.com/ru/articles/652185/
https://stackoverflow.com/questions/54813704/how-to-add-dashboard-configuration-json-file-in-grafana-image 
 /* - Что касается панели HOME, вы можете использовать переменную GF_DASHBOARDS_DEFAULT_HOME_DASHBOARD_PATH среды, как показано выше.
      Подробнее о переменной GF_DASHBOARDS_DEFAULT_HOME_DASHBOARD_PATH среды вы можете прочитать в этой https://github.com/grafana/grafana/issues/26441 записке на GitHub.
      Полный исчерпывающий пример кода вы можете найти в следующем репозитории github https://github.com/cirocosta/sample-grafana
      Оригинальная статья с пояснением есть - https://ops.tips/blog/initialize-grafana-with-preconfigured-dashboards/
 */

docker-compose up -d --build