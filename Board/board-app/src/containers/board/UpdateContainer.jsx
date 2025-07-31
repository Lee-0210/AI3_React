import Update from "../../components/board/Update"
import * as boards from '../../apis/boards'
import * as files from '../../apis/files'
import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"

function UpdateContainer() {

  // state
  const {id} = useParams()
  const [board, setBoard] = useState({})
  const navigate = useNavigate()
  const [fileList, setFileList] = useState([])

  // 게시글 조회 요청
  const getBoard = async () => {
    const response = await boards.select(id)
    const data = await response.data
    console.dir(data)
    setBoard(data.board)
    setFileList(data.fileList)
  }

  // 다운로드
  const onDownload = async (id, fileName) => {
    // API 요청
    const response = await files.download(id)
    console.log(response)

    // 1. 서버에서 응답 파일데이터를 받은 Bolb 변환
    // 2. 브라우저를 통해 a 태그로 등록
    // 3. a 태그의 다운로드 가능으로 요청
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')    // a 태그를 생성
    link.href = url
    link.setAttribute('download', fileName)
    document.body.appendChild(link)
    link.click()                                // 다운로드 기능을 가진 a 태그를 클릭
    document.body.removeChild(link)
  }

  // 게시글 수정 요청
  const onUpdate = async (data, headers) => {
    try {
      const response = await boards.update(data, headers)
      const result = await response.data
      alert(result)
      navigate('/boards')
    }
    catch (error) {
      console.error(error)
    }
  }

  useEffect(() => {
    getBoard()
  }, [])

  // 게시글 삭제 요청
  const onDelete = async (id) => {
    try {
      const response = await boards.remove(id)
      const result = await response.data
      alert(result)
      navigate('/boards')
    }
    catch(e) {
      console.error(e)
    }
  }

  // 게시글 삭제 요청
  const onDeleteFile = async (fileId) => {
    try {
      const response = await boards.remove(fileId)
      const result = await response.data
      alert(result)
      navigate('/boards')
    }
    catch(e) {
      console.error(e)
    }
  }

  // 선택 삭제 요청
  const deleteCheckedFiles = async (idList) => {
    const fileIdList = idList.join(",")
    console.log(fileIdList);
    try {
      // 파일 선택 삭제 요청
      const response = await files.removeFiles( fileIdList )
      console.log(response.data);
      // 파일 목록 갱신
      const boardResponse = await boards.select(id)
      const data = boardResponse.data
      const fileList = data.fileList
      setFileList(fileList)
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <>
      <Update id={id} board={board} fileList={fileList} onUpdate={onUpdate} onDelete={onDelete} onDownload={onDownload} deleteCheckedFiles={deleteCheckedFiles} onDeleteFile={onDeleteFile}/>
    </>
  )
}

export default UpdateContainer