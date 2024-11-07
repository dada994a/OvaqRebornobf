"C:\Program Files\Java\jdk-17\bin\java.exe" -jar PikachuObfuscator.jar input.jar input-obf.jar -asmVer 9 -fixVersion -classToFolder -useStringObf -useStringObfT -useNumberObf -useJunkCode -inClass (.*) -exClass net.shoreline.client.mixin(.*) net.shoreline.client.socket.json(.*) -obfLocalVar -delLocalVar -applymap maps.txt -reverse
pause
@echo off