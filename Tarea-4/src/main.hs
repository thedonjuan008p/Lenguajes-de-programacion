import System.Environment(getArgs)
import Data.List(elemIndex,transpose)
import Data.List.Split(splitOn)
import Data.Maybe(fromJust)

main :: IO()
main = do
    args <- getArgs
    cryptexFile <- readFile (args !! 1)
    let cryLines = lines cryptexFile
    let cryptex = parsLetters cryLines
    let lenWord = length (cryptex!!0) -- este largo servira para filtrar las palabras al parsear el diccionario.
    dicFile <- readFile (args !! 0)
    let dicLines = lines dicFile
    let palabras = parsWords dicLines lenWord
    let final = checkWords palabras (transpose cryptex)
    putStrLn $ getOut final


parsWords :: [String] -> Int -> [String]
parsWords lineas largo= let
    correctWords = filter (\x -> (splitOn "," x) !! 1 /= "count" && length ((splitOn "," x) !! 0) == largo) lineas
    palabras = [splitOn "," x | x <- correctWords]
    in [a!!0 | a <- palabras]

parsLetters :: [String] -> [[String]]
parsLetters lineas = let
    letras = [words x | x <- lineas, take 1 x /= "#"]
    in letras

checkWords :: [String] -> [[String]] -> [(String, Int)] -- Esta funcion entrega una lista con las palabras y cantidad de movimientos para formarla en el cryptex
checkWords palabras cryptex  = let
    ps = [(x, checkMoves x cryptex) | x <- palabras, checkLetters x cryptex == length x]
    in ps

checkLetters :: String -> [[String]] -> Int -- Esta funcion entrega la cantidad de letras que coinciden con su respectiva columna en el cryptex
checkLetters _ [[]] = 0
checkLetters [x] [y] = if elemIndex [x] y /= Nothing then 1 else 0
checkLetters (x:xs) (y:ys) = if elemIndex [x] y /= Nothing then 1 + checkLetters xs ys else 0

checkMoves :: String -> [[String]] -> Int -- Esta función entrega la cantidad de movimientos minimos que se necesitan para formar la palabra
checkMoves _ [[]] = 0
checkMoves [x] [y] = min (fromJust (elemIndex [x] y)) (length y - fromJust (elemIndex [x] y))
checkMoves (x:xs) (y:ys) = min (fromJust (elemIndex [x] y)) (length y - fromJust (elemIndex [x] y)) + checkMoves xs ys

getOut :: [(String, Int)] -> String -- Entrega un string con el formato para printear en consola
genOut [] = ""
genOut [(palabra,movimientos)] = palabra ++ " (" ++ show movimientos ++ " moves)"
genOut [(palabra,movimientos),(palabra2,movimientos2)] = palabra ++ " (" ++ show movimientos ++ " moves)\n"++ palabra2 ++ " (" ++ show movimientos2 ++ " moves)"
getOut ((palabra,movimientos):xs) = palabra ++ " (" ++ show movimientos ++ " moves)\n" ++ genOut xs