# 냉장고 관리 애플리케이션 '냉글벙글'
![스플레쉬](https://github.com/fridge-manager-project/backend/assets/86510667/d140af32-0c6d-4380-8506-237bae44be42)

---
## 기술 스택
### Enviroment
<div align=center> 
  <img src="https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellij&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <br>
</div>

### Development
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springBoot&logoColor=white">
  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=springSecurity&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
  <img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
  <br>
</div>

### Communication
<div align=center> 
  <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
  <br>
</div>

---
## 주요 기능
### JWT 토큰 인증과 RTR - (정완)
- 로그인 시 accessToken과 refreshToken 발급하고 refreshToken을 일정시간동안 저장
- accessToken 만료시 refreshToken rotation을 사용해 accessToken과 refreshToken을 재발급
### 소비기한 임박 상품과 장바구니 푸시 알림 - (정완)
- 스프링 스케쥴러와 사용자 기기토큰을 사용해 정해진 시간마다 Firebase Cloud Messaging으로 푸시알림 전송
- 소비기한이 임박하거나 지난 상품을 가진 사용자에게 오후 3시, 오전 10시에 알림 전송
- 장바구니에 상품이 있을 경우 오후 6시에 알림 전송
### 사용자 환경에 맞는 보관소(냉장실, 냉동실) 구성 기능 - (현석)
- 사용자가 소유한 보관소 구성을 추가
- 소유한 보관소의 구성(냉장실, 냉동실 개수)를 추가할 수 있고 이름을 설정하여 구분 가능
### 장바구니 - 구매할 상품 리스트 작성 기능 - (정완)
- 추가할 식품을 장바구니에 체크리스트로 보관
### ALB를 사용한 HTTPS 통신 구축 - (정완)
- ssl인증서와 alb 라우팅을 사용해 http요청을 https 포트로 전송
---
## 서버 아키텍쳐
<img width="1667" alt="스크린샷 2024-07-12 오전 2 30 20" src="https://github.com/fridge-manager-project/backend/assets/86510667/0e5ad88d-c1eb-4eac-9c92-363335d422cf">

---
## ERD

![Copy of 냉장고관리어플리케이션 Erd 버전1](https://github.com/user-attachments/assets/f98153f3-38f2-4026-ae60-29920e2e041a)

---
