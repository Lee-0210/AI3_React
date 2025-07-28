import React, { useState } from 'react'
import Card from './Card'

const List = () => {

  const[todoList, setTodoList] = useState(
    [
      {"id" : "id-1", "name" : "할 일 1", "status" : false},
      {"id" : "id-2", "name" : "할 일 2", "status" : false},
      {"id" : "id-3", "name" : "할 일 3", "status" : true},
      {"id" : "id-4", "name" : "할 일 4", "status" : false},
      {"id" : "id-5", "name" : "할 일 5", "status" : false},
      {"id" : "id-6", "name" : "할 일 6", "status" : false},
      {"id" : "id-7", "name" : "할 일 7", "status" : false},
    ]
  )

  const onToggle = (todo) => {
    const updateTodoList = todoList.map(item =>
      item.id == todo.id ? {...item, status : !item.status} : item
    )
    setTodoList(updateTodoList)
  }

  return (
    <div className="todoList">
      <ul>
          {
            todoList.map(todo => {
              return <Card todo={todo} onToggle={onToggle}/>
            })
          }
      </ul>
    </div>
  )
}

export default List