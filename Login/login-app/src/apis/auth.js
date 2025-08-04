import api from './api';

// 회원가입
export const join = data => api.post('/users', data);

// 로그인
export const login = (username, password) => {
  return api.get(`/login?username=${username}&password=${password}`)
}

// 회원 정보
export const info = () => api.get(`/users/info`)

