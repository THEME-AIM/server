### Git branch
- `main`:  구현 완료된 작업물이 올라오는 branch
- `feature`: 작업 중인 branch
  - 물리적인 branch가 아님
  - `main` branch에서 `feature/{작업자 명}` branch를 만들어서 작업하고, 작업이 완료되면 `main` branch에 merge함
  - branch 종류
    - `feature/sunsuking`: 민준수 branch
    - `feature/vincent`: 김민재 branch
    - `feature/kidzero`: 유아영 branch
- `refactor`: 코드 리팩토링 및 에러 방지 해결을 위한 branch
  - 물리적인 branch가 아님
  - `main` branch에서 `refactor/{기능명}` branch를 만들어서 작업하고, 작업이 완료되면 `main` branch에 merge함

![git_structure.png](git_structure.png)