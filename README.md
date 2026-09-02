# AS-AshpazYar | آشپزیار

آشپزیار یک دستیار آشپزی فارسی، آفلاین‌محور و چندسکویی از **AS Team** است که بر پایه Kotlin و Compose Multiplatform توسعه داده می‌شود.

## نسخه فعلی

- Version Name: `1.0.0`
- Android Version Code: `1`
- Android Package: `com.asteam.ashpazyar`
- iOS Bundle ID: `com.asteam.ashpazyar`
- رابط فارسی و RTL
- طراحی آپدیت‌خور؛ Package برنامه در نسخه‌های آینده نباید تغییر کند.

## قابلیت‌های نسخه 1.0.0

- ۱۵ دستور غذای فارسی و کامل به‌صورت داخلی و آفلاین
- مشاهده تصویر، معرفی، مواد لازم و مراحل پخت
- جست‌وجو بر اساس نام غذا، توضیح، ماده اولیه و برچسب
- فیلتر دسته‌بندی: کیک، شیرینی، دسر، سالم و سایر دسته‌های داده
- نمایش زمان آماده‌سازی، زمان پخت، زمان کل، تعداد نفرات و سطح سختی
- افزودن و حذف از علاقه‌مندی‌ها
- صفحه مستقل علاقه‌مندی‌ها
- ذخیره دائمی علاقه‌مندی‌ها و تنظیمات در Android با `SharedPreferences`
- حفظ داده‌های کاربر هنگام نصب نسخه جدید، با ثابت ماندن Package و امضای انتشار
- Drawer راست‌چین و آیکون‌دار مطابق استاندارد مشترک AS Team
- تنظیمات و اشتراک‌گذاری در ابتدای Drawer
- خانه، علاقه‌مندی‌ها، تنظیمات، اشتراک‌گذاری، درباره نرم‌افزار و تماس با ما
- Share Sheet واقعی Android برای معرفی برنامه
- دکمه بررسی نسخه جدید و اتصال به صفحه Releases پروژه
- پشتیبانی ساختاری از Android، iOS، Desktop، Web/PWA، Android TV و Automotive
- انیمیشن‌های Shared Element و افکت حرکتی تصویر مبتنی بر سنسور در پلتفرم‌های پشتیبانی‌شده
- CI برای Build و کنترل Android Debug/Release، TV، Automotive، Desktop و Web

## ساختار پروژه

```text
androidApp/      Android launcher
shared/          UI, models, recipes and shared business logic
iosApp/          iOS host project
desktopApp/      Desktop launcher
webApp/          Web/WASM + PWA resources
tvApp/           Android TV host
automotiveApp/   Android Automotive host
docs/            Generated/static web assets and documentation
```

## فایل‌های اصلی

- `shared/src/commonMain/kotlin/App.kt`: ناوبری اصلی، Drawer و State مشترک
- `shared/src/androidMain/kotlin/main.android.kt`: persistence، Share و Update actions در Android
- `shared/src/commonMain/kotlin/model/Recipe.kt`: مدل دستور غذا
- `shared/src/commonMain/kotlin/model/ExampleData.kt`: دیتای آفلاین دستورها
- `shared/src/commonMain/kotlin/recipeslist/RecipesList.kt`: جست‌وجو، دسته‌بندی و فهرست
- `shared/src/commonMain/kotlin/recipeslist/RecipeListItem.kt`: کارت دستور و علاقه‌مندی
- `shared/src/commonMain/kotlin/details/`: صفحه جزئیات، مواد و مراحل
- `.github/workflows/build.yml`: Build و کنترل خروجی‌ها
- `docs/FA_COMMENTING_GUIDE.md`: استاندارد کامنت‌نویسی فارسی AS Team

## Build اندروید

پیش‌نیازها: Android SDK، JDK سازگار با AGP و دسترسی Gradle به مخازن Maven.

```bash
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:assembleRelease
```

خروجی‌ها در مسیرهای زیر تولید می‌شوند:

```text
androidApp/build/outputs/apk/debug/
androidApp/build/outputs/apk/release/
```

Release تولیدشده بدون keystore اختصاصی، unsigned است. برای APK قابل انتشار باید keystore اختصاصی AS Team در محیط امن CI یا سیستم توسعه تنظیم شود. Keystore، رمزها و کلیدهای امضا نباید داخل Git ذخیره شوند.

## Build وب

```bash
./gradlew :webApp:wasmJsBrowserDistribution
```

منابع PWA در `webApp/src/wasmJsMain/resources/` قرار دارند.

## سیاست نسخه و به‌روزرسانی

برای هر انتشار جدید:

1. `versionCode` اندروید حتماً افزایش پیدا کند.
2. `versionName` مطابق نسخه انتشار تغییر کند.
3. `applicationId = com.asteam.ashpazyar` ثابت بماند.
4. امضای Release با همان کلید انتشار قبلی انجام شود.
5. کلیدهای `SharedPreferences` فعلی حذف یا بدون Migration تغییر نام داده نشوند.
6. داده‌ها و تنظیمات کاربر در Migrationهای نسخه‌های بعدی حفظ شوند.
7. قبل از Release، CI و Build نهایی بررسی شوند.

## وضعیت محصول

نسخه `1.0.0` به‌عنوان نسخه پایه قابل استفاده تکمیل شده است. قابلیت‌هایی مانند دیتابیس دستورهای قابل ویرایش توسط کاربر، لیست خرید، برنامه غذایی هفتگی، مقیاس خودکار مواد اولیه، تایمر پخت و Backup/Restore جزو توسعه‌های نسخه‌های بعدی هستند و برای عملکرد اصلی نسخه 1.0.0 الزامی نیستند.

## پشتیبانی

`AS.Developers.Support@Gmail.Com`

Develop by **AS Team Group**
