import {useEffect, useState} from 'react'
import styles from './css/Update.module.css'
import { Link } from 'react-router-dom'

const Update = ({id, board, onUpdate, onDelete}) => {

  const [title, setTitle] = useState('')
  const [writer, setWriter] = useState('')
  const [content, setContent] = useState('')

  useEffect(() => {
    setTitle(board.title)
    setWriter(board.writer)
    setContent(board.content)
  }, [board])

  // state 변경 함수
  const changeTitle = e => {
    setTitle(e.target.value)
  }
  const changeWriter = e => {
    setWriter(e.target.value)
  }
  const changeContent = e => {
    setContent(e.target.value)
  }

  // 수정 함수
  const onSubmit = () => {
    const data = {
      id: id,
      title: title,
      writer: writer,
      content: content
    }
    const headers = {'Content-Type': 'application/json'}

    onUpdate(data, headers)
  }

  // 삭제 함수
  const handleDelete = () => {
    const check = confirm('정말 삭제하시겠습니까?')
    if(!check) return
    onDelete(id)
  }
  return (
    <div className="container">
      <h1 className="title">게시글 수정</h1>
      <table className={styles.table}>
        <tr>
          <th>제목</th>
          <td>
            <input onChange={changeTitle} type="text" name="" id="" className={styles['form-input']} value={title} />
          </td>
        </tr>
        <tr>
          <th>작성자</th>
          <td>
            <input onChange={changeWriter} type="text" name="" id="" className={styles['form-input']} value={writer} />
          </td>
        </tr>
        <tr>
          <th>내용</th>
          <td colSpan={2}>
            <textarea onChange={changeContent} rows={10} name="" id="" className={styles['form-input']} value={content}></textarea>
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <div>
          <Link to="/boards" className="btn">목록</Link>
        </div>
        <div>
          <button onClick={onSubmit} className="btn">수정</button>
          <button onClick={handleDelete} className="btn">삭제</button>
        </div>
      </div>
    </div>
  )
}

export default Update