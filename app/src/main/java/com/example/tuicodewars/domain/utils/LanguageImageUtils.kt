package com.example.tuicodewars.domain.utils

import android.util.Log

// Many logo-s are not showing up very nicely in either dark or light theme
fun List<String>.generateLogoUrlsForLanguages(): List<String> {
    return this.map { language ->
        when (language) {
            "ruby" -> "https://seeklogo.com/images/R/ruby-logo-087AF79367-seeklogo.com.png"
            "java" -> "https://seeklogo.com/images/J/java-logo-7F8B35BAB3-seeklogo.com.png"
            "javascript" -> "https://seeklogo.com/images/J/javascript-logo-8892AEFCAC-seeklogo.com.png"
            "python" -> "https://seeklogo.com/images/P/python-logo-A32636CAA3-seeklogo.com.png"
            "coffeescript" -> "https://seeklogo.com/images/C/coffeescript-logo-3BFDF9D06C-seeklogo.com.png"
            "typescript" -> "https://seeklogo.com/images/T/typescript-logo-B29A3F462D-seeklogo.com.png"
            "c" -> "https://seeklogo.com/images/C/c-programming-language-logo-9B32D017B1-seeklogo.com.png"
            "cpp" -> "https://seeklogo.com/images/C/c-logo-43CE78FF9C-seeklogo.com.png"
            "csharp" -> "https://seeklogo.com/images/C/c-sharp-c-logo-02F17714BA-seeklogo.com.png"
            "php" -> "https://seeklogo.com/images/P/php-logo-DC4A01DBB6-seeklogo.com.png"
            "shell" -> "https://seeklogo.com/images/B/bash-logo-BF4F6893D9-seeklogo.com.png"
            "haskell" -> "https://seeklogo.com/images/H/haskell-logo-07C83118EA-seeklogo.com.png"
            "crystal" -> "https://seeklogo.com/images/C/crystal-logo-E9AFE86146-seeklogo.com.png"
            "rust" -> "https://seeklogo.com/images/R/rust-logo-E6517C759B-seeklogo.com.png"
            "go" -> "https://seeklogo.com/images/G/golang-go-logo-F87103EF81-seeklogo.com.png"
            "lua" -> "https://seeklogo.com/images/L/lua-logo-A416E5A66F-seeklogo.com.png"
            "elixir" -> "https://seeklogo.com/images/E/elixir-logo-CF24E6FA55-seeklogo.com.png"
            "prolog" -> "https://www.swi-prolog.org/icons/swipl.png"
            "julia" -> "https://seeklogo.com/images/J/julia-logo-FBACB021E3-seeklogo.com.png"
            "r" -> "https://seeklogo.com/images/R/r-project-logo-A101B11270-seeklogo.com.png"
            "cobol" -> "https://cacmb4.acm.org/system/assets/0003/2653/092618_Kackr.io_Cobol.large.jpg"
            "scala" -> "https://seeklogo.com/images/S/scala-logo-8570724313-seeklogo.com.png"
            "sql" -> "https://images.seeklogo.com/logo-png/52/1/sql-projekt-ag-logo-png_seeklogo-526394.png"
            "groovy" -> "https://seeklogo.com/images/G/groovy-logo-45CC24D539-seeklogo.com.png"
            "clojure" -> "https://seeklogo.com/images/C/clojure-logo-3433AFADE0-seeklogo.com.png"
            "nim" -> "https://seeklogo.com/images/N/nim-programming-language-logo-0E27D54E77-seeklogo.com.png"
            "swift" -> "https://seeklogo.com/images/S/swift-logo-7927855EB5-seeklogo.com.png"
            "fsharp" -> "https://seeklogo.com/images/F/fsharp-logo-C217FA0E41-seeklogo.com.png"
            "kotlin" -> "https://seeklogo.com/images/K/kotlin-logo-30C1970B05-seeklogo.com.png"
            "racket" -> "https://seeklogo.com/images/R/racket-logo-634E28E1B7-seeklogo.com.png"
            "dart" -> "https://seeklogo.com/images/D/dart-logo-86B5DDAA61-seeklogo.com.png"
            "factor" -> "https://andrewshitov.com/wp-content/uploads/2019/12/Factor.png"
            "raku" -> "https://seeklogo.com/images/R/raku-logo-0CA2FB2A88-seeklogo.com.png"
            "d" -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLe5kruF8klPgJkVVosz_oQhtfP0jVBrtInILCla7l1xNUOMRnzJiSrRLA2MOR3TDOHiI&usqp=CAU"
            "vb" -> "https://seeklogo.com/images/V/visual-studio-code-logo-43C3AC9C08-seeklogo.com.png"
            "commonlisp" -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrUzYVECscvcfJ6YSo6oUj-XL1FZ5ew5TTwO3XQtNH18TLkd9xO5toggXoukpLYXy9l9k&usqp=CAU"
            "nasm" -> "https://seeklogo.com/images/N/netwide-assembler-nasm-logo-EC5B1109AC-seeklogo.com.png"
            "powershell" -> "https://images.seeklogo.com/logo-png/52/2/powershell-logo-png_seeklogo-521226.png"
            "riscv" -> "https://banner2.cleanpng.com/20180505/fww/kisspng-risc-v-reduced-instruction-set-computer-instructio-5aed91f3e5b613.1307765115255188359409.jpg"
            "bf" -> "https://static-00.iconduck.com/assets.00/brainfuck-icon-512x512-mhpf1b41.png"
            "ocaml" -> "https://seeklogo.com/images/O/ocaml-logo-2201C882E2-seeklogo.com.png"
            "solidity" -> "https://seeklogo.com/images/S/solidity-logo-D29CC3EB00-seeklogo.com.png"
            "lambdacalc" -> "https://seeklogo.com/images/H/half-life-lambda-logo-F2F9875056-seeklogo.com.png"
            "cfml" -> "https://seeklogo.com/images/A/adobe-coldfusion-logo-30D3A036DE-seeklogo.com.png"
            "perl" -> "https://seeklogo.com/images/P/perl-programming-language-logo-0F38A111E5-seeklogo.com.png"
            "objc" -> "https://www.elbuild.it/assets/img/techs/objectc.png"
            else -> {
                Log.d("MissingImageFor:", language)
                "https://seeklogo.com/images/C/coding-logo-553EFA7061-seeklogo.com.png" // Fallback for unknown languages
            }
        }
    }
}