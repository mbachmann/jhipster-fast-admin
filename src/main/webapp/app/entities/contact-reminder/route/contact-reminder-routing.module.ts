import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactReminderComponent } from '../list/contact-reminder.component';
import { ContactReminderDetailComponent } from '../detail/contact-reminder-detail.component';
import { ContactReminderUpdateComponent } from '../update/contact-reminder-update.component';
import { ContactReminderRoutingResolveService } from './contact-reminder-routing-resolve.service';

const contactReminderRoute: Routes = [
  {
    path: '',
    component: ContactReminderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactReminderDetailComponent,
    resolve: {
      contactReminder: ContactReminderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactReminderUpdateComponent,
    resolve: {
      contactReminder: ContactReminderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactReminderUpdateComponent,
    resolve: {
      contactReminder: ContactReminderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactReminderRoute)],
  exports: [RouterModule],
})
export class ContactReminderRoutingModule {}
