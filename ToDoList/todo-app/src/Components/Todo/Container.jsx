import React from 'react'
import Input from './Input'
import Header from './Header'
import List from './List'
import Footer from './Footer'

const Container = () => {
  return (
    <div className="container">
      <Header/>
      <Input/>
      <List/>
      <Footer/>
    </div>
  )
}

export default Container