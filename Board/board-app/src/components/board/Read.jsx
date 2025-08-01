import {useState} from 'react'
import { Link, useParams } from 'react-router-dom'
import styles from './css/Read.module.css'
import DownloadIcon from '@mui/icons-material/Download';
// ckeditor5
import { CKEditor } from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

const Read = ({board, fileList, onDownload}) => {

  // const fileList = [
  //   {id: 'id1', originName: '파일명1', type: 'MAIN', fileSize: '2048'},
  //   {id: 'id2', originName: '파일명2', type: 'MAIN', fileSize: '2048'},
  //   {id: 'id3', originName: '파일명3', type: 'MAIN', fileSize: '2048'}
  // ]

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
            {/* <textarea  rows={10} name="" id="" className={styles['form-input']} defaultValue={board.content ?? ''} readOnly></textarea> */}
            <CKEditor
              editor={ ClassicEditor }
              data={ board.content }           // 조회할 데이터 컨텐츠
              disabled={true}
              config={{
                  toolbar: [],
              }}
            />
          </td>
        </tr>
        <tr>
          <td colSpan={2}>
            {
              fileList.map(file => (
                <div className="flex-box" key={file.id}>
                  <div className="item">
                    <div className="item-img">
                      {file.type == 'MAIN' && <span className="badge">대표</span>}
                      <img src={`/api/files/img/${file.id}`} alt={file.originName} className="file-img" />
                    </div>
                    <span>{file.originName} ({file.fileSize})</span>
                  </div>
                  <div className="item">
                    <button onClick={() => onDownload(file.id, file.originName)} className="btn"><DownloadIcon/></button>
                  </div>
                </div>
              ))
            }
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