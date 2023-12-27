# Contributing

## ⏳ Подготовка к разработке
Для начала работы нужно склонировать репозиторий и перейти в созданную директорию

```bash

git clone https://github.com/VKCOM/vkid-android-sdk.git
cd vkid-android-sdk
```
Так же можно сделать форк и склонировать его

## 🐶 Базовые команды
> Все команды запускаются из корня проекта
1. `./gradlew build` - полная сборка проекта
2. `./gradlew test` - прогон unit тестов
3. `./gradlew lint` - запуск android линта
4. `./gradlew detekt` - запуск detekt для проверки качества кода

## 🪵 Создание ветки

Ветки создаются от `master`.

Для названия веток используется специальный шаблон:

```
{task_type}/{issue_number}/{description}

Где:
- {task_type} - "fix", если это испарвление бага и "feature", если это полноценная доработка
- {issue_number} - VKIDSDK-XXX для разработчиков VK и ISSUE-XXX для внешних контрибьютеров
- {description} - Краткое описание проделанной работы
```

Пример:
```bash
git checkout master
git pull
git checkout -b feature/some-feature/ISSUE-0000
```

## 📝 Создание коммита

Сообщение в коммите должно соответствовать шаблону:

```
{issue_number}: {commit_description} 

Где:
- {issue_number} - VKIDSDK-XXX для разработчиков VK и ISSUE-XXX для внешних контрибьютеров
- {commit_description} - Краткое описание коммита на английском языке
```


Пример:
```bash
git checkout master
git add -A
git commit -m "ISSUE-0000: some commit description"
```

## 🔧 Подготовка к пушу ветки

Перед пушем ветки нужно убедиться, что обновлено публичное апи модулей и залочены зависимости
```bash
./gradlew allDependencies --write-locks
git commit -m "ISSUE-XXX: Update dependency locks"
./gradlew apiDump
git commit -m "ISSUE-XXX: Update public api"
git push
```
Обратите внимание, что в коммитах нужно указать номер issue, в рамках которой будет сделан pull request
Также проверьте, что после выполнения `./gradlew apiDump` в вайлах *.api нету изменений, ломающих обратную совместимость и 
все изменения в публичном апи обоснованы.

## 😸 Подготовка Pull Request

Заголовок мра должен быть сделан по следующему шаблону:
```
{issue_number}: {commit_description} 

Где:
- {issue_number} - VKIDSDK-XXX для разработчиков VK и ISSUE-XXX для внешних контрибьютеров
- {commit_description} - Краткое описание коммита на английском языке
```

Пример:
```
ISSUE-000: Some issue description
```
