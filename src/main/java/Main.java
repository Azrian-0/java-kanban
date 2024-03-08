import Server.HttpTaskServer;
import Server.KVServer;


public class Main {
    public static void main(String[] args) throws Exception {

        new HttpTaskServer().start();



//        {
//            "name": "Таск",
//                "description": "Таск 1",
//                "status": "IN_PROGRESS",
//                "duration": 15,
//                "startTime": "2024-02-26T18:55:00"
//        }
//       {
//           "name": "Эпик",
//               "description": "Эпик 1",
//               "status": "IN_PROGRESS",
//               "duration": 30,
//               "startTime": "2024-02-26T18:00:00"
//       }
//        {
//            "name": "Саб",
//                "description": "Саб 1",
//                "status": "IN_PROGRESS",
//                "epicId": 1,
//                "duration": 15,
//                "startTime": "2024-02-26T18:00:00"
//        }
//        {
//            "name": "Саб",
//                "description": "Саб 2",
//                "status": "IN_PROGRESS",
//                "epicId": 1,
//                "duration": 15,
//                "startTime": "2024-02-26T18:15:00"
//        }
    }
}