import React, { useEffect, useRef, useState } from 'react'
import Card from './Card'

const List = ({todoList, setTodoList, onToggle, onRemove, doneList, setDoneList}) => {

  const listRef = useRef()

  // todo OR done
  const check = todoList != null ? true : false
  console.log(`check : ${check}`)

  // state
  const [page, setPage] = useState(1)

  // 데이터 목록 추가 함수
  const addList = (page) => {
    const url = check ? `http://localhost:8080/todos?page=${page}&isDone=no`
                        : `http://localhost:8080/todos?page=${page}&isDone=yes`
    // 할 일 목록 요청
    fetch(url)
        .then(response => response.json())
        .then(data => {
          console.dir(`pagination : ${data.pagination.last}`)
          if(page > data.pagination.last) {
            console.log('마지막 페이지입니다.')
            return
          }

          // 기존 리스트에 새 페이지 목록 누적
          if(check) {
            console.log(`todoList : ${todoList}`)
            const newTodoList = [...todoList, ...data.list]
            console.log(`newTodoList : ${JSON.stringify(newTodoList, null, 2)}`)
            setTodoList(newTodoList)
          } else  {
            console.log(`doneList : ${doneList}`)
            const newTodoList = [...doneList, ...data.list]
            setDoneList(newTodoList)
          }
          setPage(page + 1)
        })
        .catch(error => console.error(error))
  }

  // 🚀 스크롤 이벤트 핸들러
  const handleScroll = () => {
    console.log('실행')
    const el = listRef.current
    const scrollHeight = el.scrollHeight   // 스크롤 높이
    const scrollTop = el.scrollTop         // 스크롤 위치
    const clientHeight = el.clientHeight   // 컨텐츠 높이

    // 스크롤 맨 마지막
    if(clientHeight + scrollTop == scrollHeight) {
      addList(page + 1)
    }
  }

  useEffect(() => {
    const el = listRef.current
    el?.addEventListener('scroll', handleScroll)

    return () => {
      el?.removeEventListener('scroll', handleScroll)
    }
  })


  return (
    <div className="todoList" ref={listRef}>
      <ul>
          {
            todoList
            ?
            todoList.map(todo => {
              return <Card todo={todo} onToggle={onToggle} onRemove={onRemove} page={page}/>
            })
            :
            doneList.map(todo => {
              return <Card todo={todo} onToggle={onToggle} onRemove={onRemove} page={page}/>
            })
          }
      </ul>
    </div>
  )
}

export default List