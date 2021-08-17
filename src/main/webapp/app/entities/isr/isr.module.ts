import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsrComponent } from './list/isr.component';
import { IsrDetailComponent } from './detail/isr-detail.component';
import { IsrUpdateComponent } from './update/isr-update.component';
import { IsrDeleteDialogComponent } from './delete/isr-delete-dialog.component';
import { IsrRoutingModule } from './route/isr-routing.module';

@NgModule({
  imports: [SharedModule, IsrRoutingModule],
  declarations: [IsrComponent, IsrDetailComponent, IsrUpdateComponent, IsrDeleteDialogComponent],
  entryComponents: [IsrDeleteDialogComponent],
})
export class IsrModule {}
