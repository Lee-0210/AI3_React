import Update from "../../components/board/Update"
import * as boards from '../../apis/boards'
import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"

function UpdateContainer() {

  // state
  const {id} = useParams()
  const [board, setBoard] = useState({})
  const navigate = useNavigate()

  // 게시글 조회 요청
  const getBoard = async () => {
    const response = await boards.select(id)
    const data = await response.data
    // console.log(`board : ${data}`)
    console.dir(data)
    setBoard(data)
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

  return (
    <>
      <Update id={id} board={board} onUpdate={onUpdate} onDelete={onDelete}/>
    </>
  )
}

export default UpdateContainer