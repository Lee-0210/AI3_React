import {useState} from 'react'
import { Link, useParams } from 'react-router-dom'
import styles from './css/Read.module.css'

const Read = ({board}) => {

  const {id} = useParams()

  return (
    <div className="container">
      <h1 className="title">게시글 조회</h1>
      <table className={styles.table}>
        <tr>
          <th>제목</th>
          <td>
            <input type="text" name="" id="" className={styles['form-input']} defaultValue={board.title ?? ''} readOnly/>
          </td>
        </tr>
        <tr>
          <th>작성자</th>
          <td>
            <input type="text" name="" id="" className={styles['form-input']} defaultValue={board.writer ?? ''} readOnly/>
          </td>
        </tr>
        <tr>
          <th>내용</th>
          <td colSpan={2}>
            <textarea  rows={10} name="" id="" className={styles['form-input']} defaultValue={board.content ?? ''} readOnly></textarea>
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">목록</Link>
        <Link to={`/boards/update/${id}`} className="btn">수정</Link>
      </div>
    </div>
  )
}

export default Read