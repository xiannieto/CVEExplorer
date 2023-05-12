# TFG
Interfaz de consulta y visualización de vulnerabilidades de BD

------- VERSIONES -------

SpringBoot: 3.0.2

ElasticSearch: elasticsearch-8.5.3

Angular: 15.2.8


---------- SERVIDOR ELASTICSEARCH ----------

Arrancar Servidor Elastic (desde cmd en la ruta del servidor): .\bin\elasticsearch.bat

Recuperar usuario

curl -u elastic http://localhost:9200

Credenciales: user/user


---------- SERVIDOR FRONT END ----------

Arrancar FRONT Angular (desde CVEExplorar/src/app): npm start


---------- COMO INICIAR EL PROYECTO ----------
Arrancar servidor Elastic
usar este comando:
      java -jar xian-0.0.1-SNAPSHOT.jar --add-cve=../cve-resources/nvdcve-1.1-2022.json
      sustituir ruta por el archivo a indexar
Arrancar Back y Front (da igual el orden)
