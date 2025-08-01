import React, { useState } from 'react'
import {Link} from 'react-router-dom'
import styles from './css/Insert.module.css'
// ckeditor5
import { CKEditor } from '@ckeditor/ckeditor5-react'
import ClassicEditor from '@ckeditor/ckeditor5-build-classic'
import * as fileApi from '../../apis/files'


const Insert = ({onInsert}) => {

  // state
  const [title, setTitle] = useState('')
  const [writer, setWriter] = useState('')
  const [content, setContent] = useState('')
  const [mainFile, setMainFile] = useState(null)
  const [files, setFiles] = useState(null)

  // 변경 이벤트 함수
  const changeTitle = e => { setTitle(e.target.value) }
  const changeWriter = e => { setWriter(e.target.value) }
  const changeContent = e => { setContent(e.target.value) }
  const changeMainFile = e => { setMainFile(e.target.files[0]) }
  const changeFiles = e => { setFiles(e.target.files) }

  // 등록 함수
  const onSubmit = () => {
    /* application/json */
    // const data = {
    //   title: title,
    //   writer: writer,
    //   content: content
    // }
    /* multipart/form-data */
    const formData = new FormData()
    formData.append('title', title)
    formData.append('writer', writer)
    formData.append('content', content)
    // 파일 데이터 세팅
    if(mainFile) formData.append('mainFile', mainFile)
    if(files) {
      for(let i = 0; i < files.length; i++) {
        const file = files[i]
        formData.append('files', file)
      }
    }
    const headers = {'Content-Type': 'multipart/form-data'}

    onInsert(formData, headers)
  }

  const customUploadAdapter = (loader) => {
    return {
      upload() {
        return new Promise( (resolve, reject) => {
          const formData = new FormData();
          loader.file.then( async (file) => {
            console.log(`파일 : ${file}`);
            formData.append("parentTable", 'editor');
            formData.append("parentNo", 0)
            formData.append("type", 'SUB')
            formData.append("seq", 0)
            formData.append("data", file);

            const headers = {
                headers: {
                    'Content-Type' : 'multipart/form-data',
                },
            };

            let response = await fileApi.upload(formData, headers);
            let data = await response.data;
            console.log(`데이터 : ${data}`);

            let id = data;

            // 이미지 렌더링
            await resolve({
                default: `/api/files/img/${id}`
            })

          });
        });
      },
    };
  };

  // CKEditor 이미지 업로드
  function uploadPlugin(editor) {
    editor.plugins.get("FileRepository").createUploadAdapter = (loader) => {
      return customUploadAdapter(loader);
    };
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
            {/* <textarea onChange={changeContent} rows={10} name="" id="" className={styles['form-input']}>

            </textarea> */}
            <CKEditor
              editor={ ClassicEditor }
              config={{
                  placeholder: "내용을 입력하세요.",
                  toolbar: {
                      items: [
                          'undo', 'redo',
                          '|', 'heading',
                          '|', 'fontfamily', 'fontsize', 'fontColor', 'fontBackgroundColor',
                          '|', 'bold', 'italic', 'strikethrough', 'subscript', 'superscript', 'code',
                          '|', 'bulletedList', 'numberedList', 'todoList', 'outdent', 'indent',
                          '|', 'link', 'uploadImage', 'blockQuote', 'codeBlock',
                          '|', 'mediaEmbed',
                      ],
                      shouldNotGroupWhenFull: false
                  },
                  editorConfig: {
                      height: 500, // Set the desired height in pixels
                  },
                  alignment: {
                      options: ['left', 'center', 'right', 'justify'],
                  },
                  extraPlugins: [uploadPlugin]            // 업로드 플러그인
              }}
              data=""         // ⭐ 기존 컨텐츠 내용 입력 (HTML)
              onReady={ editor => {
                  // You can store the "editor" and use when it is needed.
                  console.log( 'Editor is ready to use!', editor );
              } }
              onChange={ ( event, editor ) => {
                  const data = editor.getData();
                  console.log( { event, editor, data } );
                  setContent(data);
              } }
              onBlur={ ( event, editor ) => {
                  // console.log( 'Blur.', editor );
              } }
              onFocus={ ( event, editor ) => {
                  // console.log( 'Focus.', editor );
              } }
              />
          </td>
        </tr>
        <tr>
          <td>메인 파일</td>
          <td>
            <input type="file" onChange={changeMainFile} />
          </td>
        </tr>
        <tr>
          <td>서브 파일</td>
          <td>
            <input type="file" multiple onChange={changeFiles}/>
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