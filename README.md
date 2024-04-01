## Запуск
Перед запуском необходимо указать путь сохранения файлов в [application.properties](./src/main/resources/application.properties). 
По умолчанию файлы сохраняются в дирректорию в корне диска D.

## Проверка
Для удобной проверки к API был написан веб-интерфйс, запускающийся на [localhost](http://localhost:8080/).
В веб-интерфейсе отражён весь фнкционал данного REST API.

![img.png](img/img.png)

![img_1.png](img/img_1.png)

## Результат
<b>API приводит текстовый файл:</b>

GREATEST MAN IN ALIVE<br>
#Chapter one<br>
this story about awesome dude that call name is Jack<br>
##Jack's characteristics<br>
###height: 71 inch<br>
###weight: 190 pounds<br>
#Chapter two<br>
Jack was most famous man in alive<br>
his fame was greater than his popularity<br>
##Jack's patents<br>
###mosquito net<br>
###x-ray<br>
###internal combustion engine<br>

<b>К следующему виду:</b>

[<br>
"GREATEST MAN IN ALIVE",<br>
"1 Chapter one",<br>
"this story about awesome dude that call name is Jack",<br>
"1.1 Jack's characteristics",<br>
"1.1.1 height: 71 inch",<br>
"1.1.2 weight: 190 pounds",<br>
"2 Chapter two",<br>
"Jack was most famous man in alive",<br>
"his fame was greater than his popularity",<br>
"2.1 Jack's patents",<br>
"2.1.1 mosquito net",<br>
"2.1.2 x-ray",<br>
"2.1.3 internal combustion engine"<br>
]