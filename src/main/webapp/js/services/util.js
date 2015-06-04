angular.module('pistachoBizi')

    .service('util', function(){

        return ({
            genUniformList: genUniformList,
            genRandomList: genRandomList,
            genObjectList: genObjectList
        });

        function genUniformList(start,end,jump){
            var list = [];
            if (arguments.length == 3){
                for(var i = start; i < end; i+=jump){
                    list.push('Station '+ i);
                }
            }else if(arguments.length == 2){
                for(i = start; i < end; i+=1){
                    list.push('Station '+ i);
                }
            }else{
                for(i = 1; i <= start; i+=1){
                    list.push('Station '+ i);
                }
            }
            return list;
        }

        function genRandomList(length,range){
            var list = [];
            if(arguments.length==2){
                for(var i = 0; i < length; i++){
                    list.push(Math.floor(Math.random()*range)+1);
                }
            }else {
                for(i = 0; i < length; i++){
                    list.push(Math.floor(Math.random()*10)+1);
                }
            }
            return list;
        }

        function genObjectList(labels,data){
            var a = labels;
            var b = data;
            var list = [];
            if(arguments==2){
                for(var i = 0; i < a.length; i++){
                    list.push({
                        station: a[i],
                        data: b[i]
                    });
                }
            }else{
                for(var i = 0; i < 10; i++){
                    list.push({
                        station: Math.floor(Math.random()*10)+1,
                        data: Math.floor(Math.random()*100)+1
                    });
                }
            }
            return list;
        }

    });
