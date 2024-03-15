module HW1.T3
  ( Tree (..),
    tsize,
    tdepth,
    tmember,
    tinsert,
    tFromList,
  )
where

type Meta = (Int, Int)

data Tree a = Leaf | Branch Meta (Tree a) a (Tree a)
  deriving (Show)

tsize :: Tree a -> Int
tsize Leaf = 0
tsize (Branch (size, _) _ _ _) = size

tdepth :: Tree a -> Int
tdepth Leaf = 0
tdepth (Branch (_, depth) _ _ _) = depth

tmember :: (Ord a) => a -> Tree a -> Bool
tmember _ Leaf = False
tmember value (Branch _ l nodeValue r) =
  value == nodeValue
    || tmember value l
    || tmember value r

newNode :: Tree a -> a -> Tree a -> Tree a
newNode leftNode value rightNode =
  let leftDepth = tdepth leftNode
      rightDepth = tdepth rightNode
      treeDepth = 1 + max leftDepth rightDepth
      treeSize = 1 + tsize leftNode + tsize rightNode
   in tryBalance (Branch (treeSize, treeDepth) leftNode value rightNode)

leftRotation :: Tree a -> Tree a
leftRotation Leaf = Leaf
leftRotation tree@(Branch _ _ _ Leaf) = tree
leftRotation (Branch _ t1 rootValue (Branch _ t2 rightValue t3)) =
  newNode (newNode t1 rootValue t2) rightValue t3

rightRotation :: Tree a -> Tree a
rightRotation Leaf = Leaf
rightRotation tree@(Branch _ Leaf _ _) = tree
rightRotation (Branch _ (Branch _ t1 leftValue t2) rootValue t3) =
  newNode t1 leftValue (newNode t2 rootValue t3)

bigLeftRotation :: Tree a -> Tree a
bigLeftRotation Leaf = Leaf
bigLeftRotation (Branch _ left value right) =
  leftRotation (newNode left value (rightRotation right))

bigRightRotation :: Tree a -> Tree a
bigRightRotation Leaf = Leaf
bigRightRotation (Branch _ left value right) =
  rightRotation (newNode (leftRotation left) value right)

getLeft :: Tree a -> Tree a
getLeft Leaf = Leaf
getLeft (Branch _ left _ _) = left

getRight :: Tree a -> Tree a
getRight Leaf = Leaf
getRight (Branch _ _ _ right) = right

tdiff :: Tree a -> Int
tdiff Leaf = 0
tdiff (Branch _ left _ right) = tdepth left - tdepth right

tryBalance :: Tree a -> Tree a
tryBalance tree =
  case ( tdiff tree,
         tdiff (getLeft tree),
         tdiff (getRight tree),
         tdiff (getRight (getLeft tree)),
         tdiff (getLeft (getRight tree))
       ) of
    (-2, _, -1, _, _) -> leftRotation tree
    (-2, _, 0, _, _) -> leftRotation tree
    (-2, _, 1, 1, _) -> bigLeftRotation tree
    (-2, _, 1, 0, _) -> bigLeftRotation tree
    (-2, _, 1, -1, _) -> bigLeftRotation tree
    (2, 1, _, _, _) -> rightRotation tree
    (2, 0, _, _, _) -> rightRotation tree
    (2, -1, _, _, -1) -> bigRightRotation tree
    (2, -1, _, _, 0) -> bigRightRotation tree
    (2, -1, _, _, 1) -> bigRightRotation tree
    _ -> tree

tinsert :: (Ord a) => a -> Tree a -> Tree a
tinsert value Leaf = newNode Leaf value Leaf
tinsert value tree@(Branch _ left nodeValue right) =
  case compare value nodeValue of
    EQ -> tree
    LT -> newNode (tinsert value left) nodeValue right
    GT -> newNode left nodeValue (tinsert value right)

tFromList :: (Ord a) => [a] -> Tree a
tFromList = foldr tinsert Leaf
