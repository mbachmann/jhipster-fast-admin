import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderConfirmationDetailComponent } from './order-confirmation-detail.component';

describe('Component Tests', () => {
  describe('OrderConfirmation Management Detail Component', () => {
    let comp: OrderConfirmationDetailComponent;
    let fixture: ComponentFixture<OrderConfirmationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrderConfirmationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ orderConfirmation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrderConfirmationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrderConfirmationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load orderConfirmation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.orderConfirmation).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
