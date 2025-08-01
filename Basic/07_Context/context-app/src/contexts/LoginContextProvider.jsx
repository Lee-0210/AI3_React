import React, { createContext, useEffect, useState } from 'react'

export const LoginContext = createContext()

const LoginContextProvider = ({children}) => {
  // 로그인 여부
  const [isLogin, setIsLogin] = useState(false)

  // 로그아웃 함수
  const logout = () => {
    setIsLogin(false)
  }

  // 임의로, 마운트 후 3초 뒤 자동 로그인
  useEffect(() => {
    setTimeout(() => {
      setIsLogin(true)
    }, 3000)
  }, [])

  return (
    // 컨텍스트 값 지정 -> value={?, ?}
    <LoginContext.Provider value={{isLogin, logout}}>
      {children}
    </LoginContext.Provider>
  )
}

export default LoginContextProvider