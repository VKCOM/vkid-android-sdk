# Contributing

## ⏳ Подготовка к разработке
Для начала работы нужно склонировать репозиторий и перейти в созданную директорию

```bash
git clone git@gitlab.mvk.com:vk-ecosystem/shared/vk-id.git
cd android-sdk
```
Так же можно сделать форк и склонировать его

## 🐶 Базовые команды
> Все команды запускаются из корня проекта
1. `./gradlew build` - полная сборка проекта
2. `./gradlew test` - прогон unit тестов
3. `./gradlew lint` - запуск android линта
4. `./gradlew detekt` - запуск detekt для проверки качества кода

## 🪵 Создание ветки

Ветки создаются от `develop`.

Для названия веток используется специальный шаблон:

```
{username}/{task_type}/{issue_number}/{description}

Где:
- {username} - Ник пользователя в свободном формате, латиницей
- {task_type} - "fix", если это испарвление бага и "feature", если это полноценная доработка
- {issue_number} - VKIDSDK-XXX для разработчиков VK и ISSUE-XXX для внешних контрибьютеров
- {description} - Краткое описание проделанной работы
```

Пример:
```bash
git checkout develop
git pull
git checkout -b u.name/feature/some-feature/ISSUE-0000
```

Для веток будет сделана проверка формата в хуке `commit-msg`, которая провалидирует соответствие названия ветки шаблону

## 📝 Создание коммита

Сообщение в коммите должно соответствовать шаблону:

```
{issue_number}: {commit_description} 

Где:
- {issue_number} - VKIDSDK-XXX для разработчиков VK и ISSUE-XXX для внешних контрибьютеров
- {commit_description} - Краткое описание коммита на английском языке
```

Для коммитов будет добавлен линтер на хук `commit-msg`, который проверяет, соответствует ли сообщение в коммите шаблону

Пример:
```bash
git checkout develop
git add -A
git commit -m "ISSUE-0000: some commit description"
```

## 😸 Подготовка Merge Request

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

Для того, чтобы подготовить Merge Request, необходимо пройти [Чеклист](.gitlab/Default.md)

## 🚅 Релизы версий
### <span style="color:green">TODO, добавить после публикации релизов</span>

## 🖊️ Документация
### <span style="color:green">TODO, добавить после поддержки генерации документации</span>