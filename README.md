# Обезличенная сборка - без моделей, с частично хаотичными названиями переменных
Единственный тестовый класс src\test\java\sZZZ\HistoryTest.java

#Запуск тестов
mvn clean test -Denv=dev Dgroups=50 -Pdev -s settings.xml -Dmaven.wagon.http.ssl.insecure=true
 
#Построение локально отчетов
mvn allure:serve

#запуск одного теста 
mvn clean -Dtest=TestPack#test14 test -Pdev
,где TestPack - имя класса в котором тест лежит, а test14 имя метода

#генерация моделей производиться по swagger контракту/спецификации
При помощи утилиты плугина openapi-generator-maven-plugin
В пом файле задать ключ inputSpec на ссылку на контракт - можно ссылкой на сваггер
после выполнить
mvn clean compile -Popenapi-generator -Pdev
и забрать сгенерированные файлы в generated-sources


