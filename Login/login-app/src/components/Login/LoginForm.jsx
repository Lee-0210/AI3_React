import { useContext } from 'react'
import { LoginContext } from '../../contexts/LoginContextProvider'
import './LoginForm.css'

const LoginForm = () => {

  // 🚚 LoginContext - 로그인 함수
  const {login} = useContext(LoginContext)

  const onLogin = e => {
    e.preventDefault()
    const form = e.target
    const username = form.username.value
    const password = form.password.value

    login(username, password)
  }

  return (
    <>
      <div className="form">
        <h2 className="login-title">로그인</h2>
        <form className="login-form" onSubmit={(e) => onLogin(e)}>
          {/* username */}
          <div>
            <label htmlFor="username">username</label>
            <input type="text" name="username" id="username" placeholder="username" autoComplete="username" required/>
          </div>
          {/* password */}
          <div>
            <label htmlFor="password">password</label>
            <input type="password" id="password" placeholder="password" autoComplete="password"/>
          </div>
          <button type="submit" className="btn btn--form btn-login">로그인</button>
        </form>
      </div>
    </>
  )
}

export default LoginForm