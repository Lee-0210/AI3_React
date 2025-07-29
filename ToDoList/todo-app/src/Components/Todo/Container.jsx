import { useEffect, useState } from 'react'
import Input from './Input'
import Header from './Header'
import List from './List'
import Footer from './Footer'

const Container = () => {

  // state
  const [input, setInput] = useState('')

  const[todoList, setTodoList] = useState([])

  // 🚀 체크박스 토글 함수
  const onToggle = async (todo, size = 10) => {
    // 할 일 여부 수정 요청
    const data = {
      ...todo,
      status: !todo.status
    }

    const option = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    }

    try {
      const url = 'http://localhost:8080/todos'
      const response = await fetch(url, option)
      const msg = await response.text()
      if(msg === 'SUCCESS') console.log('할 일 수정 완료')
      else if(msg === 'FAIL') console.log('할 일 수정 실패')
      getList(size)
    } catch(e) {
      console.log(e)
    }
  }

  // 🚀 할 일 입력 변경 함수
  const onChange = (e) => {
    console.log(e.target.value)
    setInput(e.target.value)
  }

  // 🚀 할 일 추가 함수
  const onSubmit = async (e) => {
    e.preventDefault()  // 기본 이벤트 동작 방지
    let name = input    // 할 일
    if(input === '') name = '제목없음'

    // 데이터 등록 요청
    const data = {
      name : name,
      seq : 1
    }
    const option = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    }

    try {
      const url = 'http://localhost:8080/todos'
      const response = await fetch(url, option)
      const msg = await response.text() // SUCCESS OR FAIL
      if(msg === 'SUCCESS') console.log("할 일 등록 성공")
      else if(msg === 'FAIL') console.log("할 일 등록 실패")

      // 할 일 목록 요청
      getList()

      // 입력 값 비우기
      setInput('')
    } catch(e) {
      console.error(e)
    }
  }

  // 🚀 할 일 목록 요청
  const getList = (size = 10) => {
    const url = `http://localhost:8080/todos?size=${size}`

    fetch(url)
        .then(response => response.json())
        .then(data => {
          // data.list        : 할 일 목록
          // data.pagination  : 페이지 정보
          console.log(data)
          setTodoList(data.list)
        })
        .catch(error => {console.error(error)})
  }

  // 🚀 할 일 삭제 함수
  const onRemove = async (id) => {
    const option = {
      method: 'DELETE',
      headers: {
        'Content-Type' : 'application/json'
      }
    }

    try {
      const url = `http://localhost:8080/todos/${id}`
      const response = await fetch(url, option)
      const msg = await response.text()
      if(msg === 'SUCCESS') console.log("할 일 삭제 성공")
      else if(msg === 'FAIL') console.log("할 일 삭제 실패")
      getList()
    } catch(e) {
      console.log(e)
    }
  }

  // 🚀 전체 삭제 함수
  const onDeleteAll = async () => {
    const url = 'http://localhost:8080/todos/all'
    const option = {
      method: 'DELETE'
    }

    try {
      const response = await fetch(url, option)
      const msg = await response.text()
      if(msg === 'SUCCESS') alert("전체 삭제 성공")
      else if(msg === 'FAIL') console.log("전체 삭제 실패")
      getList()
    } catch(e) {
      console.error(e)
    }
  }

  // 🚀 전체 완료 함수
  const onCompleteAll = async () => {
    const url = 'http://localhost:8080/todos/all'
    const option = {
      method: 'PUT'
    }

    try {
      const response = await fetch(url, option)
      const msg = await response.text()
      if(msg === 'SUCCESS') alert("전체 완료 성공")
      else if(msg === 'FAIL') console.log("전체 완료 실패")
      getList()
    } catch(e) {
      console.error(e)
    }
  }


  // 컴포넌트가 마운트 될 때 초기 렌더링
  useEffect(() => {
    getList()
  }, [])

  return (
    <div className="container">
      <Header/>
      <Input input={input} onChange={onChange} onSubmit={onSubmit}/>
      <List todoList={todoList} onToggle={onToggle} onRemove={onRemove} setTodoList={setTodoList}/>
      <Footer onDeleteAll={onDeleteAll} onCompleteAll={onCompleteAll}/>
    </div>
  )
}

export default Container