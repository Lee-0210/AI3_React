import React from 'react'

const Input = ({input, onChange, onSubmit}) => {
  return (
    <div>
      <form onSubmit={onSubmit} className="form">
        <input value={input} type="text" name="" id="" placeholder="할 일 입력"
        className="input" onChange={onChange} />
        <button type="submit" className="btn">추가</button>
      </form>
    </div>
  )
}

export default Input