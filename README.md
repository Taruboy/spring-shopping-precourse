# spring-shopping-precourse

## 구현할 기능 목록

### 상품
- [ ] 상품 추가 `POST /api/products`
- [ ] 상품 조회 `GET /api/products/{productId}`
- [ ] 상품 목록 조회 `GET /api/products`
- [ ] 상품 수정 `PUT /api/products/{productId}`
- [ ] 상품 삭제 `DELETE /api/products/{productId}`

### 상품 유효성 검사 및 예외 처리
- [ ] 상품 이름은 공백 포함 최대 15자
- [ ] 상품 이름에는 `( ) [ ] + - & / _` 외의 특수 문자 사용 불가
- [ ] 상품 이름에 비속어 포함 불가 (PurgoMalum API로 검사)
- [ ] 잘못된 값이면 적절한 오류 응답 반환

### 회원 로그인
- [ ] 회원 가입 `POST /api/members/register` (이메일, 비밀번호)
- [ ] 로그인 `POST /api/members/login` (검증 후 토큰 발급)

### 위시 리스트 (인증된 사용자 대상)
- [ ] 위시 리스트 상품 추가 `POST /api/wishes`
- [ ] 위시 리스트 상품 삭제 `DELETE /api/wishes/{wishId}`
- [ ] 위시 리스트 조회 `GET /api/wishes`

## 구현 전략

- 기능 목록의 한 줄을 하나의 커밋 단위로 삼아, 테스트 작성 → 구현 → 커밋 순서로 진행한다.
- Controller(요청 처리), Service(규칙 처리), Repository(저장)로 역할을 나눈다.
- 저장소는 먼저 메모리에 구현하고, Repository를 통해서만 접근한다.
- 상품 이름 규칙은 추가와 수정이 함께 쓰도록 한 곳에서 검사한다.
- 비속어 검사는 인터페이스로 분리하고, 테스트에서는 가짜 구현으로 대체한다.
- 로그인 시 발급한 토큰을 요청 헤더로 받아, 위시 리스트 API에서 회원을 식별한다.
- 예외는 한곳에서 상태 코드(400, 401, 404 등)로 변환해 응답한다.

## 패키지 구조

기능(도메인) 기준으로 나누고, 여러 기능이 함께 쓰는 것은 `global`에 둔다.

```
shopping
├── member        회원 (controller, service, repository, domain)
├── product       상품 (controller, service, repository, domain, client)
├── wish          위시 리스트 (controller, service, repository, domain)
└── global
    ├── auth      토큰 발급·검증, 로그인한 회원 식별
    └── exception 예외 종류, 전역 예외 처리, 오류 응답 형식
```

- 호출 방향은 `controller → service → repository` 한 방향으로만 한다.
- `product/client`에는 비속어 검사(PurgoMalum) 외부 API 연동을 둔다.
- `wish`는 `member`, `product`를 참조할 수 있고, 반대는 허용하지 않는다.

## 인증(토큰) 전략

1. 회원 가입 또는 로그인에 성공하면 서버가 토큰을 발급해 응답한다.
2. 클라이언트는 이후 요청의 `Authorization` 헤더에 `Bearer {토큰}` 형태로 토큰을 보낸다.
3. 인증이 필요한 API(`/api/wishes/**`)는 Controller에 도달하기 전에 토큰을 검사하고 회원을 식별한다.
4. 토큰이 없거나 유효하지 않으면 401을 응답한다.
5. Controller는 식별된 회원을 전달받아 사용만 하고, 토큰을 직접 해석하지 않는다.

- 토큰 방식: JWT를 사용한다. 토큰에 회원 식별 정보를 담아 서버에 토큰을 따로 저장하지 않는다.
- 비밀번호는 평문이 아닌 해시로 저장하고, 로그인 시 입력값을 같은 방식으로 변환해 비교한다.
- 로그인 실패 시 이메일 오류와 비밀번호 오류를 구분하지 않고 같은 응답을 준다.
- 위시 삭제 시 해당 항목의 주인이 로그인한 회원인지 확인하고, 아니면 403을 응답한다.

## AI 도구 활용 기록

- 활용한 방식:
- 수정한 부분:
- 학습한 내용: