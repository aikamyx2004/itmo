# Курс "Функциональное программирование" на КТ

## Полезные ссылки

* [Страница с материалами курса](https://github.com/jagajaga/FP-Course-ITMO).
* [Диск с записями лекций](https://drive.google.com/drive/u/1/folders/1S17GlvEM4ehtUVujQjPPtfVE7bqJt141).
* [Папка с условиями домашних заданий](/Homework/).
* [Папка с условиями заданий для практики](/Practice/).
* [Табличка с баллами](https://docs.google.com/spreadsheets/d/1NVkwuSjx6SSkREZmfWVYK-wu7Uv0jgRf5ojmOuw_weo/edit#gid=0).
* [Style-guide](code-style.md).
* [Рекомендуемая конфигурация stylish-haskell](.stylish-haskell.yaml).
* [Рекомендации по настройке рабочего окружения](environment-setup.md).

## Процедура сдачи домашних заданий

Решения домашних заданий необходимо загрузить в ваш личный репозиторий GitHub Classroom строго до указанного дедлайна. Ваше решение будет проходить автоматическое тестирование, которое запускается на CI после каждого пуша в `master`. Вы можете ознакомиться с его результатами в соответствующей вкладке на GitHub. После дедлайна преподавателями будет проведено код-ревью и проверка решений на списывание. В конечном итоге баллы за каждое из заданий и комментарии от преподавателей будут опубликованы в таблице курса.

Для каждого из домашних заданий при помощи GitHub Classroom для вас будет создан личный репозиторий с корректной структурой проекта. Конфигурация автотестов находится в директории `.github` в этом репозитории. Изменение содержимого данной папки **строго запрещено**.

**Важно:**

1. Система автоматической проверки ДЗ компилирует ваше решение с флагом `-Werror`, что влечет ошибку компиляции при наличии warning'ов от GHC. Рекомендуем отправлять решение на проверку, заранее убедившись в их отсутствии.
2. Как тестирующей системой, так и преподавателями оценивается только **последний коммит** в вашем репозитории, будьте внимательны на этот счет.
3. В ходе код-ревью **преподавателями оцениваются решения всех заданий**, вне зависимости от прохождения тестов по этим заданиям на CI (i.e. если ваше решение не проходит тесты по какому-то из заданий, вы все еще можете получить как положительные, так и отрицательные баллы за качество решения).

## Настройка рабочего окружения для выполнения домашних заданий

При выполнении ДЗ рекомендуем ознакомиться с общим документом по [настройке рабочего окружения](environment-setup.md).

Автоматические тесты используют `ghc-8.10.7`. Рекомендуем при выполнении домашних заданий локально установить именно эту версию компилятора для избежания потенциальных проблем со сборкой проекта другой версией.