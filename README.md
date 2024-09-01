# READMEというよりメモ

# [JumpStartGuide](https://typelevel.org/cats/jump_start_guide.html)の章を写経
してるけど、`???`部分の実装が面倒くさそうな章はスキップしてる

# 代数的性質と用語
- Associative(結合的)
    - 二項演算子`⊕`が結合的であるとは、`a ⊕ (b ⊕ c)`と`(a ⊕ b) ⊕ c`が等しいことを意味します。
    - つまり、演算の順序を変えても結果は変わりません。
- Commutative(交換的)
    - 二項演算子`⊕`が交換的であるとは、`a ⊕ b`と`b ⊕ a`が等しいことを意味します。
    - つまり、引数の順序を入れ替えても結果は変わりません。
- Identity(単位元)
    - 値`id`が演算子`⊕`の単位元であるとは、`a ⊕ id`と`id ⊕ a`がどちらも`a`と等しいことを意味します。
    - 単位元を演算に含めても結果は変わりません。
- Inverse(逆元)
    - `¬a`が`a`の逆元であり、`id`が単位元であるとは、`a ⊕ ¬a`と`¬a ⊕ a`がどちらも`id`と等しいことを意味します。
    - 要素とその逆元を演算すると、単位元になります。
- Distributive(分配的)
    - 二項演算子`⊕`と`⊙`が分配的であるとは、`a ⊙ (b ⊕ c)` = `(a ⊙ b) ⊕ (a ⊙ c)`かつ`(a ⊕ b) ⊙ c` = `(a ⊙ c) ⊕ (b ⊙ c)`が成り立つことを意味します。
    - つまり、演算子を分配して計算しても結果は変わりません。
- Idempotent(冪等的)
    - 二項演算子`⊕`が冪等的であるとは、`a ⊕ a` = `a`が成り立つことを意味します。
    - 演算子`f`が冪等的であるとは、`f(f(a))` = `f(a)`が成り立つことを意味します。
    - つまり、値や関数に2回以上適用しても結果は変わりません。

## 記号について
`⊕`などの記号は、一般的な二項演算子(ふたつの値に対して作用する演算)を表すために使われています。
具体的には
- `⊕` は加算や論理和などの二項演算を表します。
- `⊙` は乗算や論理積などの二項演算を表します。
- `¬` は否定や加法に対する逆元を表します。
  これらの記号は、具体的な演算ではなく、任意の二項演算を抽象的に表すために使われています。それにより、結合律、交換律、分配律などの性質を一般化して表現できます。
  例えば、実数の加算に対しては、`⊕`を`+`と読み替えることで性質が成り立ちます。 一方、論理演算に対しては、`⊕`を`OR`、`⊙`を`AND`と読み替えることで同様の性質が成り立ちます。
  つまり、`⊕`や`⊙`はあくまで一般的な記号であり、具体的にどの演算を表すかはコンテクストによって変わります。抽象代数などの分野ではこのような一般化された記号が多用されています。

# Tutorials(ドキュメント読んだらこれをやろう)
- [herding cats](http://eed3si9n.com/herding-cats/) by Eugene Yokota
- [Scala Exercises](https://www.scala-exercises.org/cats) by 47 Degrees offers multiple online exercises to learn about the type classes and datatypes included in Cats.

