# 봉투 가계부

개인용 안드로이드 가계부 앱. 월급을 봉투(분류)별로 나눠 두고, 카드 결제 알림을 읽어 자동으로 기록·분류해 해당 봉투에서 차감한다.

- 데이터는 휴대폰 안(SharedPreferences)에만 저장되고 서버로 보내지 않는다.
- `main`에 푸시하면 GitHub Actions가 APK를 빌드해 Releases에 올린다.
- 화면: `app/src/main/assets/index.html` / 카드 알림 읽기: `CardListener.java`
