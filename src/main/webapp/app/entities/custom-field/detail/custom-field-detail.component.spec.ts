import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomFieldDetailComponent } from './custom-field-detail.component';

describe('Component Tests', () => {
  describe('CustomField Management Detail Component', () => {
    let comp: CustomFieldDetailComponent;
    let fixture: ComponentFixture<CustomFieldDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CustomFieldDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ customField: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CustomFieldDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomFieldDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load customField on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.customField).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
