import { useState } from 'react'
import './App.css'
import Home from './pages/Home'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import LoginContextProvider from './contexts/LoginContextProvider'
import About from './pages/About'
import Join from './pages/Join'
import Login from './pages/Login'
import User from './pages/User'

function App() {

  return (
    <>
      <BrowserRouter>
        <LoginContextProvider>
          <Routes>
            <Route path='/' element={<Home/>}/>
            <Route path='/about' element={<About/>}/>
            <Route path='/join' element={<Join/>}/>
            <Route path='/login' element={<Login/>}/>
            <Route path='/user' element={<User/>}/>
          </Routes>
        </LoginContextProvider>
      </BrowserRouter>
    </>
  )
}

export default App
