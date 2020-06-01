package com.edumage.bmstu_enrollee.Data;

import android.util.ArrayMap;

import com.edumage.bmstu_enrollee.CafedraItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CafedraNames {
    private static ArrayMap<String, ArrayMap <String, CafedraItem>> nameCafedra;

    public CafedraNames() {

        nameCafedra = new ArrayMap<>();
        ArrayMap<String, CafedraItem> mt, iu, rl, fn, sm, e, rk, bmt, ibm, l, oep, rkt, ak, ps, rt, sgn, ur, fvo, guimc, fmop, fof;

        mt=new ArrayMap<>();
        mt.put("МТ1", new CafedraItem("Металлорежущие станки"));
        mt.put("МТ2", new CafedraItem("Инструментальная техника и технологии"));
        mt.put("МТ3", new CafedraItem("Технологии машиностроения"));
        mt.put("МТ4", new CafedraItem("Метрология и взаимозаменяемость"));
        mt.put("МТ5", new CafedraItem("Литейные технологии"));
        mt.put("МТ6", new CafedraItem("Технологии обработки давлением"));
        mt.put("МТ7", new CafedraItem("Технологии сварки и диагностики"));
        mt.put("МТ8", new CafedraItem("Материаловедение"));
        mt.put("МТ9", new CafedraItem("Промышленный дизайн"));
        mt.put("МТ10", new CafedraItem("Оборудование и технологии прокатки"));
        mt.put("МТ11", new CafedraItem("Электронные технологии в машиностроении"));
        mt.put("МТ12", new CafedraItem("Лазерные технологии в машиностроении"));
        mt.put("МТ13", new CafedraItem("Технологии обработки материалов"));
        nameCafedra.put("МТ",mt);

        iu=new ArrayMap<>();
        iu.put("ИУ1", new CafedraItem("Системы автоматического управления"));
        iu.put("ИУ2", new CafedraItem("Приборы и системы ориентации, стабилизации и навигации"));
        iu.put("ИУ3", new CafedraItem("Информационные системы и телекоммуникации"));
        iu.put("ИУ4", new CafedraItem("Конструирование и производство электронной аппаратуры"));
        iu.put("ИУ5", new CafedraItem("Системы обработки информации и управления"));
        iu.put("ИУ6", new CafedraItem("Компьютерные системы и сети"));
        iu.put("ИУ7", new CafedraItem("Программное обеспечение ЭВМ и информационные технологии"));
        iu.put("ИУ8", new CafedraItem("Информационная безопасность"));
        iu.put("ИУ9", new CafedraItem("Теоретическая информатика и компьютерные технологии"));
        iu.put("ИУ10", new CafedraItem("Защита информации"));
        nameCafedra.put("ИУ",iu);

        rl=new ArrayMap<>();
        rl.put("РЛ1", new CafedraItem("Радиоэлектронные системы и устройства"));
        rl.put("РЛ2", new CafedraItem("Лазерные и оптико-электронные системы"));
        rl.put("РЛ6", new CafedraItem("Технологии приборостроения"));
        nameCafedra.put("РЛ",rl);

        fn=new ArrayMap<>();
        fn.put("ФН1", new CafedraItem("Высшая математика"));
        fn.put("ФН2", new CafedraItem("Прикладная математика"));
        fn.put("ФН4", new CafedraItem("Физика"));
        fn.put("ФН11", new CafedraItem("Вычислительная математика и математическая физика"));
        fn.put("ФН12", new CafedraItem("Математическое моделирование"));
        nameCafedra.put("ФН",fn);

        sm=new ArrayMap<>();
        sm.put("СМ1", new CafedraItem("Космические аппараты и ракеты-носители"));
        sm.put("СМ2", new CafedraItem("Аэрокосмические системы"));
        sm.put("СМ3", new CafedraItem("Динамика и управление полетом ракет и космических аппаратов"));
        sm.put("СМ4", new CafedraItem("Высокоточные летательные аппараты"));
        sm.put("СМ5", new CafedraItem("Автономные информационные и управляющие системы"));
        sm.put("СМ6", new CafedraItem("Ракетные и импульсные системы"));
        sm.put("СМ7", new CafedraItem("Специальная робототехника и мехатроника"));
        sm.put("СМ8", new CafedraItem("Стартовые ракетные комплексы"));
        sm.put("СМ9", new CafedraItem("Многоцелевые гусеничные машины и мобильные роботы"));
        sm.put("СМ10", new CafedraItem("Колесные машины"));
        sm.put("СМ11", new CafedraItem("Подводные роботы и аппараты"));
        sm.put("СМ12", new CafedraItem("Технологии ракетно-космического машиностроения"));
        sm.put("СМ13", new CafedraItem("Ракетно-космические композиционные конструкции"));
        nameCafedra.put("СМ",sm);

        e=new ArrayMap<>();
        e.put("Э1", new CafedraItem("Ракетные двигатели"));
        e.put("Э2", new CafedraItem("Поршневые двигатели"));
        e.put("Э3", new CafedraItem("Газотурбинные и нетрадиционные энергоустановки"));
        e.put("Э4", new CafedraItem("Холодильная, криогенная техника системы кондиционирования и жизнеобеспечения"));
        e.put("Э5", new CafedraItem("Вакуумная и компрессорная техника"));
        e.put("Э6", new CafedraItem("Теплофизика"));
        e.put("Э7", new CafedraItem("Ядерные реакторы и установки"));
        e.put("Э8", new CafedraItem("Плазменные энергетические установки"));
        e.put("Э9", new CafedraItem("Экология и промышленная безопасность"));
        e.put("Э10", new CafedraItem("Гидромеханика, гидромашины и гидропневмоавтоматика"));
        nameCafedra.put("Э",e);

        // TODO: add educational plans for the following faculties
//        rk=new ArrayMap<>();
//        rk.put("РК1", new CafedraItem("Инженерная графика"));
//        rk.put("РК2", new CafedraItem("Теория механизмов и машин"));
//        rk.put("РК3", new CafedraItem("Основы конструирования машин"));
//        rk.put("РК4", new CafedraItem("Подъемно-транспортные системы"));
//        rk.put("РК5", new CafedraItem("Прикладная механика"));
//        rk.put("РК6", new CafedraItem("Системы автоматизированного проектирования"));
//        rk.put("РК9", new CafedraItem("Компьютерные системы автоматизации производства"));
//        nameCafedra.put("РК",rk);
//
//        bmt=new ArrayMap<>();
//        bmt.put("БМТ1", new CafedraItem("Биомедицинские технические системы"));
//        bmt.put("БМТ2", new CafedraItem("Медико-технические информационные технологии"));
//        bmt.put("БМТ4", new CafedraItem("Медико-технический менеджмент"));
//        nameCafedra.put("БМТ",bmt);
//
//        ibm=new ArrayMap<>();
//        ibm.put("ИБМ1", new CafedraItem("Экономическая теория"));
//        ibm.put("ИБМ2", new CafedraItem("Экономика и организация производства"));
//        ibm.put("ИБМ3", new CafedraItem("Промышленная логистика"));
//        ibm.put("ИБМ4", new CafedraItem("Менеджмент"));
//        ibm.put("ИБМ5", new CafedraItem("Финансы"));
//        ibm.put("ИБМ6", new CafedraItem("Предпринимательство и внешнеэкономическая деятельность"));
//        ibm.put("ИБМ7", new CafedraItem("Инновационное предпринимательство"));
//        nameCafedra.put("ИБМ",ibm);
//
//        l=new ArrayMap<>();
//        l.put("Л1", new CafedraItem("Русский язык"));
//        l.put("Л2", new CafedraItem("Английский язык для приборостроительных специальностей"));
//        l.put("Л3", new CafedraItem("Английский язык для машиностроительных специальностей"));
//        l.put("Л4", new CafedraItem("Романо-германские языки"));
//        nameCafedra.put("Л",l);
//
//        oep=new ArrayMap<>();
//        oep.put("ОЭ2", rl.get("РЛ2"));
//        nameCafedra.put("ОЭП",oep);
//
//        rkt=new ArrayMap<>();
//        rkt.put("РКТ1 - ИУ-1", new CafedraItem("Системы автоматического управления"));
//        rkt.put("РКТ2 - СМ-1", new CafedraItem("Космические аппараты и ракеты-носители"));
//        rkt.put("РКТ3 - МТ-2", new CafedraItem("Инструментальная техника и технологии"));
//        rkt.put("РКТ4 - СМ-12", new CafedraItem("Технология ракетно-космического машиностроения"));
//        rkt.put("РКТ5 - Э-1", new CafedraItem("Ракетные двигатели"));
//        nameCafedra.put("РКТ",rkt);
//
//        ak=new ArrayMap<>();
//        ak.put("АК1 - СМ-2", new CafedraItem("Аэрокосмические системы"));
//        ak.put("АК2 - СМ-2", new CafedraItem("Аэрокосмические системы"));
//        ak.put("АК3 - ФН-11", new CafedraItem("Вычислительная математика и математическая физика"));
//        ak.put("АК4 - ИУ-1", new CafedraItem("Системы автоматического управления"));
//        nameCafedra.put("АК",ak);
//
//        ps=new ArrayMap<>();
//        ps.put("ПС1 - ИУ-2", new CafedraItem("Приборы и системы ориентации, стабилизации и навигации"));
//        ps.put("ПС2 - ИУ-1", new CafedraItem("Системы автоматического управления"));
//        ps.put("ПС3 - СМ-8", new CafedraItem("Стартовые ракетные комплексы"));
//        ps.put("ПС4 - ИУ-11", new CafedraItem("Космические приборы и системы"));
//        nameCafedra.put("ПС",ps);
//
//        rt=new ArrayMap<>();
//        rt.put("РТ1 - РЛ-1", new CafedraItem("Радиоэлектронные системы и устройства"));
//        rt.put("РТ2 - ИУ-4", new CafedraItem("Проектирование и технология производства аппаратуры"));
//        rt.put("РТ4 - ИУ-1", new CafedraItem("Системы автоматического управления"));
//        rt.put("РТ5 - ИУ-5", new CafedraItem("Системы обработки информации и управления"));
//        nameCafedra.put("РТ",rt);
//
//        sgn=new ArrayMap<>();
//        sgn.put("СГН1", new CafedraItem("История"));
//        sgn.put("СГН2", new CafedraItem("Социология и культурология"));
//        sgn.put("СГН3", new CafedraItem("Информационная аналитика и политические технологии"));
//        sgn.put("СГН4", new CafedraItem("Философия"));
//        nameCafedra.put("СГН",sgn);
//
//        ur=new ArrayMap<>();
//        ur.put("ЮР1", new CafedraItem("Интеллектуальная собственность"));
//        ur.put("ЮР2", new CafedraItem("Цифровая криминалистика"));
//        ur.put("ЮР3", new CafedraItem("Правоведение"));
//        nameCafedra.put("ЮР",ur);
//
//        fvo=new ArrayMap<>();
//        fvo.put("Кафедра №1", new CafedraItem("Кафедра радиотехнических войск"));
//        fvo.put("Кафедра №2", new CafedraItem("Кафедра космических войск и противоракетной обороны"));
//        fvo.put("Кафедра №3", new CafedraItem("Кафедра зенитных ракетных войск"));
//        fvo.put("Кафедра №4", new CafedraItem("Кафедра специальной подготовки"));
//        fvo.put("Кафедра №5", new CafedraItem("Кафедра общевойсковой подготовки"));
//        fvo.put("Кафедра №6", new CafedraItem("Кафедра военно-воздушных сил(Мытищинский филиал)"));
//        fvo.put("Кафедра №7", new CafedraItem("Кафедра связи(Калужский филиал)"));
//        nameCafedra.put("ФВО",fvo);
//
//        guimc=new ArrayMap<>();
//        guimc.put("ИУ1-Ц", iu.get("ИУ1"));
//        guimc.put("ИУ3-Ц", iu.get("ИУ3"));
//        guimc.put("ИУ4-Ц", iu.get("ИУ4"));
//        guimc.put("ИУ5-Ц", iu.get("ИУ5"));
//        guimc.put("ИУ6-Ц", iu.get("ИУ6"));
//        guimc.put("ИУ8-Ц", iu.get("ИУ8"));
//        guimc.put("МТ2-Ц", mt.get("МТ2"));
//        guimc.put("МТ4-Ц", mt.get("МТ4"));
//        guimc.put("МТ8-Ц", mt.get("МТ8"));
//        guimc.put("МТ12-Ц", mt.get("МТ12"));
//        guimc.put("РК9-Ц", rk.get("РК9"));
//        guimc.put("РЛ1-Ц", rl.get("РЛ1"));
//        guimc.put("ФН11-Ц", fn.get("ФН11"));
//        guimc.put("Э9-Ц", e.get("Э9"));
//        nameCafedra.put("ГУИМЦ",guimc);
//
//        fmop=new ArrayMap<>();
//        fmop.putAll(bmt);
//        fmop.putAll(ibm);
//        fmop.putAll(iu);
//        fmop.putAll(mt);
//        fmop.putAll(rk);
//        fmop.putAll(rl);
//        fmop.putAll(sgn);
//        fmop.putAll(sm);
//        fmop.putAll(fn);
//        fmop.putAll(e);
//        fmop.putAll(ur);
//        fmop.remove("БМТ4"); fmop.remove("БМТ4");
//        fmop.remove("ИБМ1"); fmop.remove("ИБМ2"); fmop.remove("ИБМ3");
//        fmop.remove("ИУ8"); fmop.remove("ИУ11");
//        fmop.remove("МТ4"); fmop.remove("МТ6"); fmop.remove("МТ7"); fmop.remove("МТ9"); fmop.remove("МТ10");
//        fmop.remove("РК1"); fmop.remove("РК4");
//        fmop.remove("РЛ1"); fmop.remove("РЛ5");
//        fmop.remove("СГН1"); fmop.remove("СГН2"); fmop.remove("СГН4");
//        fmop.remove("СМ4"); fmop.remove("СМ5"); fmop.remove("СМ6"); fmop.remove("СМ8"); fmop.remove("СМ9"); fmop.remove("СМ11");
//        fmop.remove("ФН3"); fmop.remove("ФН5"); fmop.remove("ФН7");
//        fmop.remove("ФН3"); fmop.remove("ФН3"); fmop.remove("ФН3");
//        fmop.remove("Э3"); fmop.remove("Э7");
//        fmop.remove("ЮР1"); fmop.remove("ЮР3");
//        nameCafedra.put("ФМОП",fmop);
//
//        fof=new ArrayMap<>();
//        fof.put("ФОФ-1",new CafedraItem("Физическое воспитание"));
//        fof.put("ФОФ-2",new CafedraItem("Здоровьесберегающие технологии и адаптивная физкультура"));
//        nameCafedra.put("ФОФ",fof);
    }

    public ArrayMap<String, ArrayMap <String, CafedraItem>> getData() {
        return nameCafedra;
    }
}
