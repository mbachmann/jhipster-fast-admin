import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IsrFaService } from '../service/isr-fa.service';

import { IsrFaComponent } from './isr-fa.component';

describe('Component Tests', () => {
  describe('IsrFa Management Component', () => {
    let comp: IsrFaComponent;
    let fixture: ComponentFixture<IsrFaComponent>;
    let service: IsrFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IsrFaComponent],
      })
        .overrideTemplate(IsrFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IsrFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(IsrFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.isrs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
