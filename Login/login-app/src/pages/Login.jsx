import React from 'react'
import Header from '../components/Header/Header'
import { useNavigate } from 'react-router-dom'
import LoginForm from '../components/Login/LoginForm'


const Login = () => {

  return (
    <>
      <Header />
      <div className="container">
        <LoginForm />
      </div>
    </>
  )
}

export default Login