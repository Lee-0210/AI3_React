import {useState} from 'react'

const Card = ({todo, onToggle}) => {
  // state
  // const [status, setStatus] = useState(todo.status)

  let {id, name, status} = todo
  let isActive = status ? 'todoItem active' : 'todoItem'

  // 체크박스 변경 핸들러 -> List 부모 컴포넌트 전환
  // const onChange = (e) => {
  //   const newStatus = e.target.checked
  //   setStatus(newStatus)
  // }

  return (
    <li className={isActive}>
      <div className="item">
        <input type="checkbox" id={id} checked={status}
                onChange={() => onToggle(todo)}/>
        <label htmlFor={id}></label>
        <span>{name}</span>
      </div>
      <div className="item">
        <button className="btn">삭제</button>
      </div>
    </li>
  )
}

export default Card