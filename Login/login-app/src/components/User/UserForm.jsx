import { useContext, useEffect } from 'react'
import './userForm.css'
import { LoginContext } from '../../contexts/LoginContextProvider'

const UserForm = ({userInfo, updateUser, deleteUser}) => {

  const {api} = useContext(LoginContext)

  // 정보 수정
  const onUpdate = e => {
    e.preventDefault()

    const form = e.target
    const username = form.username.value
    const password = form.password.value
    const name = form.name.value
    const email = form.email.value
    console.log(`username : ${username}, password : ${password}, name : ${name}, email : ${email}`)

    updateUser({username, password, name, email})
  }

  useEffect(() => {
    console.log(`api : ${api}`)
    console.log(`default : ${api.defaults.headers.common.Authorization}`)
  }, [])

  return (
    <div className="form">
      <h2 className="login-title">
        <form className="login-form" onSubmit={ e => onUpdate(e) }>
          <div>
            <label htmlFor="username">username</label>
            <input type="text" name="username" id="username" placeholder='username' autoComplete="username" required readOnly defaultValue={userInfo.username} />
          </div>
          <div>
            <label htmlFor="password">password</label>
            <input type="password" name="password" id="password" placeholder='password' autoComplete="password" required />
          </div>
          <div>
            <label htmlFor="name">name</label>
            <input type="text" name="name" id="name" placeholder='name' autoComplete="name" required defaultValue={userInfo.name} />
          </div>
          <div>
            <label htmlFor="email">email</label>
            <input type="email" name="email" id="email" placeholder='email' autoComplete="email" required defaultValue={userInfo.email} />
          </div>
          <button type="submit" className="btn btn--form btn-log">정보수정</button>
          <button type="button" className="btn btn--form btn-log" onClick={() => deleteUser(userInfo.username)}>회원 탈퇴</button>
        </form>
      </h2>
    </div>
  )
}

export default UserForm