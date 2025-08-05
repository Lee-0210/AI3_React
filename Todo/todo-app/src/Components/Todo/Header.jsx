import React from 'react'

const Header = ({isDone}) => {
  return (
    <h1 className="header">
      {
        isDone ? 'Done List' : 'To Do List'
      }
    </h1>
  )
}

export default Header