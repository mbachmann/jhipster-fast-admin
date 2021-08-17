import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderConfirmationFaDetailComponent } from './order-confirmation-fa-detail.component';

describe('Component Tests', () => {
  describe('OrderConfirmationFa Management Detail Component', () => {
    let comp: OrderConfirmationFaDetailComponent;
    let fixture: ComponentFixture<OrderConfirmationFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrderConfirmationFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ orderConfirmation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrderConfirmationFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrderConfirmationFaDetailComponent);
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
