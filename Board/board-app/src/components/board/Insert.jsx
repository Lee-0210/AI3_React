import React, { useState } from 'react';
import {Link} from 'react-router-dom'
import styles from './css/Insert.module.css'

const Insert = ({onInsert}) => {

  // state
  const [title, setTitle] = useState('')
  const [writer, setWriter] = useState('')
  const [content, setContent] = useState('')

  // 변경 이벤트 함수
  const changeTitle = e => {
    setTitle(e.target.value)
  }
  const changeWriter = e => {
    setWriter(e.target.value)
  }
  const changeContent = e => {
    setContent(e.target.value)
  }

  // 등록 함수
  const onSubmit = () => {
    const data = {
      title: title,
      writer: writer,
      content: content
    }
    const headers = {'Content-Type': 'application/json'}

    onInsert(data, headers)
  }

  return (
    <div className="container">
      <h1 className="title">게시글 쓰기</h1>
      <table className={styles.table}>
        <tr>
          <th>제목</th>
          <td>
            <input onChange={changeTitle} type="text" name="" id="" className={styles['form-input']} />
          </td>
        </tr>
        <tr>
          <th>작성자</th>
          <td>
            <input onChange={changeWriter} type="text" name="" id="" className={styles['form-input']} />
          </td>
        </tr>
        <tr>
          <th>내용</th>
          <td colSpan={2}>
            <textarea onChange={changeContent} rows={10} name="" id="" className={styles['form-input']}></textarea>
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">목록</Link>
        <button onClick={onSubmit} className="btn">등록</button>
      </div>
    </div>
  )
}

export default Insert