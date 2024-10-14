# Technical commands

Во время разработки проекта используются несколько технических команд. Ознакомьтесь с ними, если вы планируете контрибьютить в наше SDK.

- `./gradlew detekt`
Запускает detekt на проекте и выводит список ошибок
- `./gradlew lint`
Запускает android lint в рамках SDK и выводит список ошибок
- `./gradlew testDebugUnitTest`
Запускает unit тесты
- `./gradlew connectedAndroidTest`
Запускает ui тесты
- `./gradlew apiDump`
Обновляет файлы .api и отображает в них актуальное состояние SDK. Нужно запускать перед тем, как открывать пр в репозиторий
- `./gradlew apiCheck`
Проверяет, что публичное апи SDK соотвествует тому, что отображено в .api файлах
- `./gradlew updateDebugScreenshotTest`
Обновлет скриншоты. Обязательно запустите перед созданием пра
- `./gradlew validateDebugScreenshotTest`
Запускает проверку screenshot тестов
- `./scripts/git/validate_git_history TARGET_BRANCH SOURCE_BRANCH`
Проверяет названия веток и историю коммитов на соответсвтие нашему формату. Нужно запускать перед открытием пра
- `./gradlew :build-logic:dokka-skip:publishToMavenLocal & ./gradlew dokkaHtmlMultiModule`
Запускает генерацию документации. Обязательно запускать перед открытием пра
- `./gradlew generateBaselineProfiles`
Обновляет baseline prfofile-ы. Перед открытием пра нужно его запустить
- `./scripts/renovate/renovate.sh`
Обновляет зависимости renovate-ом, требует запущенного докера