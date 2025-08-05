import React from 'react'

const Footer = ({onDeleteAll, onCompleteAll}) => {
  return (
    <footer>
      <div className="item">
        <button onClick={onDeleteAll} className="btn">전체삭제</button>
      </div>
      <div className="item">
        <button onClick={onCompleteAll} className="btn">
          전체완료
        </button>
      </div>
    </footer>
  )
}

export default Footer