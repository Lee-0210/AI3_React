import './App.css'
import ProductDetail from './Components/ProductDetail'

function App() {

  // ✅ 객체 추가
    const product = {
        productId : 'p000001',
        name : '반팔 티셔츠',
        price : 35000,
        quantity : 1,
        img: 'https://i.imgur.com/1vpSkbW.png',
    }

  return (
    <>
      <ProductDetail product={product}/>
    </>
  )
}

export default App
