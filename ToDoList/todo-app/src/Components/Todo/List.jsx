import React, { useEffect, useState } from 'react'
import Card from './Card'

const List = ({todoList, setTodoList, onToggle, onRemove}) => {

  // state
  const [page, setPage] = useState(1)

  // 데이터 목록 추가 함수
  const addList = (page) => {
    // 할 일 목록 요청
    fetch(`http://localhost:8080/todos?page=${page}`)
        .then(response => response.json())
        .then(data => {
          console.log(data)
          if(page > data.pagination.last) {
            alert('마지막 페이지입니다.')
            return
          }

          // 기존 리스트에 새 페이지 목록 누적
          const newTodoList = [...todoList, ...data.list]
          setTodoList(newTodoList)
          setPage(page + 1)
        })
        .catch(error => console.error(error))
  }

  // 🚀 스크롤 이벤트 핸들러
  const handleScroll = () => {
    const todoListElement = document.querySelector('.todoList')
    const scrollHeight = todoListElement.scrollHeight   // 스크롤 높이
    const scrollTop = todoListElement.scrollTop         // 스크롤 위치
    const clientHeight = todoListElement.clientHeight   // 컨텐츠 높이

    // 스크롤 맨 마지막
    if(clientHeight + scrollTop > scrollHeight) {
      addList(page + 1)
    }
  }

  useEffect(() => {
    // 스크롤 이벤트 등록
    const todoListElement = document.querySelector('.todoList')
    if(todoListElement)
      todoListElement.addEventListener('scroll', handleScroll)
    return () => {
      if(todoListElement) {
        todoListElement.removeEventListener('scroll', handleScroll)
      }
    }
  })

  return (
    <div className="todoList">
      <ul>
          {
            todoList.map(todo => {
              return <Card todo={todo} onToggle={onToggle} onRemove={onRemove} page={page}/>
            })
          }
      </ul>
    </div>
  )
}

export default List