import React, { useState, useEffect } from 'react';
import Insert from '../../components/board/Insert';
import * as boards from '../../apis/boards';
import { useNavigate } from 'react-router-dom';

function InsertContainer() {

  const navigate = useNavigate()

  const onInsert = async (data, headers) => {
    try {
      const response = await boards.insert(data, headers)
      const result = await response.data
      console.log(result)
      alert('등록완료')

      // 게시글 목록으로 이동
      navigate('/boards')
    } catch(e) {
      console.error(e)
    }
  }

  return (
    <>
      <Insert onInsert={onInsert}/>
    </>
  )
}

export default InsertContainer;