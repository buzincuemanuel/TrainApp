package com.buzincuemanuel.trainapp.controller;

import com.buzincuemanuel.trainapp.dto.DelayDto;
import com.buzincuemanuel.trainapp.dto.RouteDto;
import com.buzincuemanuel.trainapp.dto.TrainDto;
import com.buzincuemanuel.trainapp.model.Route;
import com.buzincuemanuel.trainapp.model.Train;
import com.buzincuemanuel.trainapp.service.RouteService;
import com.buzincuemanuel.trainapp.service.TrainScheduleService;
import com.buzincuemanuel.trainapp.service.TrainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TrainService trainService;
    private final RouteService routeService;
    private final TrainScheduleService trainScheduleService;

    @GetMapping("/trains")
    public String manageTrains(Model model) {
        model.addAttribute("trains", trainService.findAll());
        model.addAttribute("trainForm", new TrainDto());
        return "admin/trains";
    }

    @PostMapping("/trains")
    public String addTrain(@ModelAttribute TrainDto form) {
        Train train = Train.builder()
                .name(form.getName())
                .capacity(form.getCapacity())
                .build();
        trainService.save(train);
        return "redirect:/admin/trains?success=true";
    }

    @PostMapping("/trains/{id}/delete")
    public String deleteTrain(@PathVariable Long id) {
        trainService.deleteById(id);
        return "redirect:/admin/trains?deleted=true";
    }

    @GetMapping("/trains/{id}/edit")
    public String editTrainForm(@PathVariable Long id, Model model) {
        Train train = trainService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid train id: " + id));
        TrainDto form = new TrainDto();
        form.setName(train.getName());
        form.setCapacity(train.getCapacity());
        model.addAttribute("trainForm", form);
        model.addAttribute("trainId", id);
        return "admin/train-edit";
    }

    @PostMapping("/trains/{id}/edit")
    public String editTrain(@PathVariable Long id, @ModelAttribute TrainDto form) {
        Train train = Train.builder()
                .name(form.getName())
                .capacity(form.getCapacity())
                .build();
        trainService.update(id, train);
        return "redirect:/admin/trains?updated=true";
    }

    @GetMapping("/routes")
    public String manageRoutes(Model model) {
        model.addAttribute("routes", routeService.findAll());
        model.addAttribute("routeForm", new RouteDto());
        return "admin/routes";
    }

    @PostMapping("/routes")
    public String addRoute(@ModelAttribute RouteDto form) {
        Route route = Route.builder()
                .name(form.getName())
                .build();
        routeService.save(route);
        return "redirect:/admin/routes?success=true";
    }

    @PostMapping("/routes/{id}/delete")
    public String deleteRoute(@PathVariable Long id) {
        routeService.deleteById(id);
        return "redirect:/admin/routes?deleted=true";
    }

    @GetMapping("/routes/{id}/edit")
    public String editRouteForm(@PathVariable Long id, Model model) {
        Route route = routeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid route id: " + id));
        RouteDto form = new RouteDto();
        form.setName(route.getName());
        model.addAttribute("routeForm", form);
        model.addAttribute("routeId", id);
        return "admin/route-edit";
    }

    @PostMapping("/routes/{id}/edit")
    public String editRoute(@PathVariable Long id, @ModelAttribute RouteDto form) {
        Route route = Route.builder()
                .name(form.getName())
                .build();
        routeService.update(id, route);
        return "redirect:/admin/routes?updated=true";
    }


    @GetMapping("/schedules")
    public String manageSchedules(Model model) {
        model.addAttribute("schedules", trainScheduleService.findAll());
        model.addAttribute("delayForm", new DelayDto());
        return "admin/schedules";
    }

    @PostMapping("/schedules/{id}/delay")
    public String updateDelay(@PathVariable Long id, @ModelAttribute DelayDto form) {
        trainScheduleService.updateDelay(id, form.getDelayMinutes());
        return "redirect:/admin/schedules?success=true";
    }

    @GetMapping("/schedules/{id}/bookings")
    public String viewBookings(@PathVariable Long id, Model model) {
        trainScheduleService.findById(id).ifPresent(schedule -> {
            model.addAttribute("schedule", schedule);
            model.addAttribute("bookings", schedule.getBookings());
        });
        return "admin/bookings";
    }
}