module HW5.Evaluator
  ( eval
  ) where

import Control.Monad.Except (ExceptT, runExceptT, MonadError (throwError))
import HW5.Base

eval :: Monad m => HiExpr -> m (Either HiError HiValue)
eval expr = runExceptT (eval' expr)


eval' :: Monad m => HiExpr -> ExceptT HiError m HiValue
eval' (HiExprValue v)                                      = return v
eval' (HiExprApply (HiExprValue (HiValueFunction f)) args) = applyFunction f args
eval' _                                                    = throwError HiErrorInvalidFunction

applyFunction :: Monad m => HiFun -> [HiExpr] -> ExceptT HiError m HiValue
applyFunction HiFunDiv [a, b] = evalDiv a b
applyFunction HiFunMul [a, b] = binary (*) a b
applyFunction HiFunAdd [a, b] = binary (+) a b
applyFunction HiFunSub [a, b] = binary (-) a b
applyFunction _ _             = throwError HiErrorArityMismatch


evalDiv :: Monad m => HiExpr -> HiExpr -> ExceptT HiError m HiValue
evalDiv a b = do
  aVal <- eval' a
  bVal <- eval' b
  case (aVal, bVal) of
    (HiValueNumber x, HiValueNumber y) ->
      if y == 0 then
        throwError HiErrorDivideByZero
      else
        return (HiValueNumber (x / y))
    _ -> throwError HiErrorInvalidArgument

binary :: Monad m => (Rational -> Rational -> Rational) -> HiExpr -> HiExpr -> ExceptT HiError m HiValue
binary f first second = do
  aVal <- eval' first
  bVal <- eval' second
  case (aVal, bVal) of
    (HiValueNumber x, HiValueNumber y) -> return (HiValueNumber (f x y))
    _                                  -> throwError HiErrorInvalidArgument